package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class UserRepositoryTests {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void testAddNewUser(){
        UserEntity user = new UserEntity();
        user.setPassword("1234567890");
        user.setPhone("9031232323");
        user.setName("Tester");
        user.setEmail("testing@mailtest.org");
        user.setHash(String.valueOf(user.getEmail().hashCode()));
        user.setRegTime(LocalDateTime.now());

        assertNotNull(userRepository.save(user));
    }
}