package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AboutPageController {

    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public AboutPageController(BookStoreUserRegister bookStoreUserRegister) {
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @GetMapping("/about")
    public String documentsPage(){
        return "/about";
    }
}

