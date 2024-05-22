package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;

@Controller
public class TagsController {

    private final BookService bookService;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public TagsController(BookService bookService, BookStoreUserRegister bookStoreUserRegister) {
        this.bookService = bookService;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @GetMapping(value = {"/tags", "/tags/{tagName}"})
    public String gettTagPage(@PathVariable(value = "tagName", required = false) String tagName, Model model) throws ParseException {
        model.addAttribute("tagName", tagName);
        if (tagName != null){
            model.addAttribute("books", bookService.getBooksByTag(0, 20, tagName).getContent());
        }
        return "/tags/index";
    }
}
