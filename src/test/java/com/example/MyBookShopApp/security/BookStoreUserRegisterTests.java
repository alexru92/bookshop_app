package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.ContactConfirmationPayload;
import com.example.MyBookShopApp.data.ContactConfirmationResponse;
import com.example.MyBookShopApp.data.RegistrationForm;
import com.example.MyBookShopApp.data.UserRepository;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookStoreUserRegisterTests {

    private final BookStoreUserRegister bookStoreUserRegister;
    private final PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;

    @MockBean
    private UserRepository userRepositoryMock;

    @MockBean
    private AuthenticationManager authenticationManagerMock;

    @Autowired
    BookStoreUserRegisterTests(BookStoreUserRegister bookStoreUserRegister, PasswordEncoder passwordEncoder) {
        this.bookStoreUserRegister = bookStoreUserRegister;
        this.passwordEncoder = passwordEncoder;
    }


    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@test.ru");
        registrationForm.setName("tester");
        registrationForm.setPassword("123456");
        registrationForm.setPhone("9111234567");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUser() {
        UserEntity user = bookStoreUserRegister.registerNewUser(registrationForm);
        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPassword(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));

        Mockito.verify(userRepositoryMock, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void authorizeUser() {
        UserEntity testUser = new UserEntity();
        testUser.setEmail("test@test.ru");
        testUser.setPassword("123456");

        ContactConfirmationPayload contactConfirmationPayload = new ContactConfirmationPayload();
        contactConfirmationPayload.setContact(testUser.getEmail());
        contactConfirmationPayload.setCode(testUser.getPassword());

        Mockito
                .when(userRepositoryMock.findUserEntityByEmail("test@test.ru"))
                .thenReturn(testUser);

        ContactConfirmationResponse contactConfirmationResponse = bookStoreUserRegister.jwtLogin(contactConfirmationPayload);
        assertNotNull(contactConfirmationResponse);

        Mockito.verify(authenticationManagerMock, Mockito.times(1))
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void registerNewUserFail(){
        Mockito.doReturn(new UserEntity())
                .when(userRepositoryMock)
                .findUserEntityByEmail(registrationForm.getEmail());

        UserEntity user = bookStoreUserRegister.registerNewUser(registrationForm);
        assertNull(user);
    }

    @Test
    void authorizeUserFail() {
        UserEntity testUser = new UserEntity();
        testUser.setEmail("test@test.ru");
        testUser.setPassword("123456");

        ContactConfirmationPayload contactConfirmationPayload = new ContactConfirmationPayload();
        contactConfirmationPayload.setContact(testUser.getEmail());
        contactConfirmationPayload.setCode(testUser.getPassword());

        boolean result = false;
        try {
            bookStoreUserRegister.jwtLogin(contactConfirmationPayload);
        } catch (UsernameNotFoundException exception){
            result = true;
        }
        assert result : "A non-existing user was unexpectedly authenticated";
    }
}