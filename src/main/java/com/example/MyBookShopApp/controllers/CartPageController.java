package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookRepository;
import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartPageController {

    private final BookRepository bookRepository;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public CartPageController(BookRepository bookRepository, BookStoreUserRegister bookStoreUserRegister) {
        this.bookRepository = bookRepository;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }


    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cookieContent", required = false) String cookieContent,
                                    Model model) {
        if (cookieContent != null && !cookieContent.equals("")) {
            cookieContent = cookieContent.startsWith("/") ? cookieContent.substring(1) : cookieContent;
            cookieContent = cookieContent.endsWith("/") ? cookieContent.substring(0, cookieContent.length() - 1) :
                    cookieContent;
            String[] cookieSlugs = cookieContent.split("/");
            List<String> suitableCookieSlugs = Arrays.stream(cookieSlugs)
                    .filter(s -> s.contains("CART"))
                    .map(s -> s.replace("CART=",""))
                    .collect(Collectors.toList());
            List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(suitableCookieSlugs);
            model.addAttribute("bookCart", booksFromCookieSlugs);
        }

        return "cart";
    }
}


