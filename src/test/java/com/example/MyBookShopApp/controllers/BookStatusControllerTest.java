package com.example.MyBookShopApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class BookStatusControllerTest {

    private final MockMvc mockMvc;
    private final String bookSlug = "book-vey-0769";

    @Autowired
    BookStatusControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleChangeBookStatus() throws Exception {
        mockMvc
                .perform(post("/book/changeBookStatus/{slug}", bookSlug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"booksId\": \"" + bookSlug + "\", \"status\": \"CART\"}")
                        .cookie(new Cookie("cookieContent", ""))
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + bookSlug))
                .andExpect(cookie().value("cookieContent", "CART=" + bookSlug));
    }

    @Test
    void handleRemoveBookRequest() throws Exception {
        mockMvc
                .perform(post("/book/changeBookStatus/remove/{slug}", bookSlug)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"booksId\": \"remove/" + bookSlug + "\", \"status\": \"UNLINK_CART\"}")
                        .cookie(new Cookie("cookieContent", "CART=" + bookSlug))
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(cookie().value("cookieContent", ""));
    }
}