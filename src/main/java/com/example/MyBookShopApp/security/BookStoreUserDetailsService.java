package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.UserRepository;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.jwt.ExpiredTokens;
import com.example.MyBookShopApp.security.jwt.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookStoreUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public BookStoreUserDetailsService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity bookStoreUser = userRepository.findUserEntityByEmail(s);
        if (bookStoreUser != null) {
            return new BookStoreUserDetails(bookStoreUser);
        } else {
            throw new UsernameNotFoundException("Such user was not found");
        }
    }

    public boolean tokenExpired(String token){
        List<ExpiredTokens> expiredTokens = tokenRepository.findAllNotOlderThanTime(LocalDateTime.now().minusHours(10));
        return expiredTokens.stream().map(s -> s.getToken().equals(token)).findAny().isPresent();
    }
}
