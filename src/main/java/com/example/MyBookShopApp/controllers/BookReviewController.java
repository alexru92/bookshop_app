package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class BookReviewController {

    private final BookService bookService;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public BookReviewController(BookService bookService, BookStoreUserRegister bookStoreUserRegister) {
        this.bookService = bookService;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @PostMapping("/bookReview")
    public String saveNewComment(@RequestBody Map<String,String> allParams, Model model) {

        bookService.saveBookComment(allParams.get("bookId"), allParams.get("text"), (UserEntity) model.getAttribute("curUsr"));
        return "redirect:/books/" + allParams.get("bookId");
    }

    @PostMapping("/rateBookReview")
    public String rateBookReview(@RequestBody Map<String,String> allParams, Model model) {
        UserEntity user = (UserEntity) model.getAttribute("curUsr");
        if (user != null){
            bookService.saveCommentLike(Integer.parseInt(allParams.get("reviewid")), Short.parseShort(allParams.get("value")), user);
        }
        return "redirect:/books/" + allParams.get("bookid");
    }
}
