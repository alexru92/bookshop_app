package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

@Controller
@RequestMapping("/book")
public class BookStatusController {

    private final BookService bookService;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public BookStatusController(BookService bookService, BookStoreUserRegister bookStoreUserRegister) {
        this.bookService = bookService;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @PostMapping("/changeBookStatus/remove/{slug}")
    public String handleRemoveBookRequest(@PathVariable("slug") String slug, @RequestBody Map<String,String> allParams, @CookieValue(name =
            "cookieContent", required = false) String cookieContent, HttpServletResponse response) {
        if (cookieContent != null && !cookieContent.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cookieContent.split("/")));
            cookieBooks.remove(allParams.get("status").replace("UNLINK_", "") + "=" + slug);
            Cookie cookie = new Cookie("cookieContent", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "redirect:/cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug, @RequestBody Map<String,String> allParams, @CookieValue(name = "cookieContent",
            required = false) String cookieContent, HttpServletResponse response) {

        if (cookieContent == null || cookieContent.equals("")) {
            Cookie cookie = new Cookie("cookieContent", allParams.get("status") + "=" + slug);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else if (!cookieContent.contains(allParams.get("status") + "=" + slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cookieContent).add(allParams.get("status") + "=" + slug);
            Cookie cookie = new Cookie("cookieContent", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return "redirect:/books/" + slug;
    }

    @PostMapping("/changeBookStatus")
    public String handleChangeRating(@RequestBody Map<String,String> allParams, @CookieValue(name = "votingAllowed",
            required = false) String votingAllowed, HttpServletResponse response, Model model) {

        String slug = allParams.get("bookId");
        String value = allParams.get("value");
        if (votingAllowed == null || votingAllowed.equals("")) {
            Cookie cookie = new Cookie("votingAllowed", slug + "=false");
            cookie.setPath("/book");
            response.addCookie(cookie);
            bookService.updadeBookRating(slug, Integer.parseInt(value), (UserEntity) model.getAttribute("curUsr"));
        } else if (!votingAllowed.contains(slug + "=false")) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(votingAllowed).add(slug + "=false");
            Cookie cookie = new Cookie("votingAllowed", stringJoiner.toString());
            cookie.setPath("/book");
            response.addCookie(cookie);
            bookService.updadeBookRating(slug, Integer.parseInt(value), (UserEntity) model.getAttribute("curUsr"));
        }
        return "redirect:/books/" + slug;
    }
}
