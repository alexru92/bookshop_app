package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.ContactConfirmationPayload;
import com.example.MyBookShopApp.data.ContactConfirmationResponse;
import com.example.MyBookShopApp.data.RegistrationForm;
import com.example.MyBookShopApp.data.UserRepository;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class BookStoreUserRegister {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookStoreUserDetailsService bookStoreUserDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public BookStoreUserRegister(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookStoreUserDetailsService bookStoreUserDetailsService, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookStoreUserDetailsService = bookStoreUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public UserEntity registerNewUser(RegistrationForm form){
        if(userRepository.findUserEntityByEmail(form.getEmail()) == null){
            UserEntity user = new UserEntity();
            user.setName(form.getName());
            user.setEmail(form.getEmail());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            user.setPhone(form.getPhone());
            user.setHash(String.valueOf(form.getEmail().hashCode()));
            user.setRegTime(LocalDateTime.now());
            user.setBalance(0);

            userRepository.save(user);
            return user;
        }
        return null;
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload contactConfirmationPayload) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        contactConfirmationPayload.getContact(),
                        contactConfirmationPayload.getCode()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload contactConfirmationPayload) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        contactConfirmationPayload.getContact(),
                        contactConfirmationPayload.getCode()
                )
        );
        BookStoreUserDetails userDetails = (BookStoreUserDetails) bookStoreUserDetailsService.loadUserByUsername(contactConfirmationPayload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public UserEntity getCurrentUser(){
        Object user = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (user instanceof String) return null;
        BookStoreUserDetails bookStoreUserDetails;
        try {
            bookStoreUserDetails = (BookStoreUserDetails) user;
        } catch (ClassCastException exception){
            DefaultOidcUser defaultOidcUser = (DefaultOidcUser) user;
            UserEntity userFromTable = userRepository.findUserEntityByEmail(defaultOidcUser.getEmail());

            if (userFromTable != null){
                bookStoreUserDetails = new BookStoreUserDetails(userFromTable);
            } else {
                StringBuilder passwordBuilder = new StringBuilder(10);
                for (int i = 0; i < 10; i++) {
                    int randomLimitedInt = 33 + (int) (new Random().nextFloat() * (126 - 33 + 1));
                    passwordBuilder.append((char) randomLimitedInt);
                }

                UserEntity userEntity = new UserEntity();
                userEntity.setHash(String.valueOf(defaultOidcUser.getAttributes().get("name").hashCode()));
                userEntity.setRegTime(LocalDateTime.now());
                userEntity.setBalance(0);
                userEntity.setName((String) defaultOidcUser.getAttributes().get("name"));
                userEntity.setEmail(defaultOidcUser.getEmail());
                userEntity.setPassword(passwordBuilder.toString());
                userEntity.setPhone(defaultOidcUser.getPhoneNumber());
                userRepository.save(userEntity);
                bookStoreUserDetails = new BookStoreUserDetails(userEntity);
            }
        }
        return bookStoreUserDetails.getBookStoreUser();
    }
}
