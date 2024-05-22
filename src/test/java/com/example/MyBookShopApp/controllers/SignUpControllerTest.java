package com.example.MyBookShopApp.controllers;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class SignUpControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    SignUpControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void signupPage() throws Exception {
        mockMvc
                .perform(post("/reg")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                                new BasicNameValuePair("name", "test"),
                                new BasicNameValuePair("email", "test@test.te"),
                                new BasicNameValuePair("phone", "123456789101"),
                                new BasicNameValuePair("password", "123456")
                        ))))
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void correctLoginWithEmailTest() throws Exception {
        mockMvc
                .perform(
                        formLogin("/signin")
                                .user("test@test.te")
                                .password("123456")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithUserDetails("kfranzotto8b@dell.com")
    void logout() throws Exception {
        mockMvc
                .perform(get("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin"));
    }

//    @Test
//    void correctLoginWithPhoneTest() throws Exception {
//        mockMvc
//                .perform(
//                        formLogin("/signin")
//                                .user("+7 (811) 546-60-32")
//                                .password("123456")
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/"));
//    }
}