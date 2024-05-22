package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.customErrors.TokenExpiredException;
import com.example.MyBookShopApp.security.BookStoreUserDetails;
import com.example.MyBookShopApp.security.BookStoreUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;

    public JWTRequestFilter(BookStoreUserDetailsService bookStoreUserDetailsService, JWTUtil jwtUtil) {
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            try {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                        username = jwtUtil.extractUsername(token);
                        if (bookStoreUserDetailsService.tokenExpired(token)) {
                            throw new TokenExpiredException();
                        }
                    }

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        BookStoreUserDetails userDetails = (BookStoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(username);
                        if (jwtUtil.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());

                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    }
                }
            } catch (ExpiredJwtException | MalformedJwtException | UsernameNotFoundException | TokenExpiredException ignore) {}
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
