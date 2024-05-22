package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.security.BookStoreUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class MyLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    public MyLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String token = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }
        if (token != null) {
            ExpiredTokens expiredToken = new ExpiredTokens();
            expiredToken.setToken(token);
            expiredToken.setTime(LocalDateTime.now());

            tokenRepository.save(expiredToken);
        }
    }
}
