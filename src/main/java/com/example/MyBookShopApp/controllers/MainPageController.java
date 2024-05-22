package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.*;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public MainPageController(BookService bookService, BookStoreUserRegister bookStoreUserRegister) {
        this.bookService = bookService;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks(){ return bookService.getPageOfRecommendedBooks(0, 6).getContent(); }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() throws ParseException { return bookService.getPageOfRecentBooks(0, 6, null, null).getContent(); }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks(){ return bookService.getPageOfPopularBooks(0, 6).getContent(); }

    @ModelAttribute("tags")
    public HashMap<String, String> tags(){return bookService.getTags();}

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }


    @GetMapping("/")
    public String mainPage(){
        return "index";
    }

}
