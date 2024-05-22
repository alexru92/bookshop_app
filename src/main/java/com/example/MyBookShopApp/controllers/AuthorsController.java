package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.struct.author.Author;
import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
public class AuthorsController {

    private final AuthorService authorService;
    private final BookStoreUserRegister bookStoreUserRegister;

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @Autowired
    public AuthorsController(AuthorService authorService, BookStoreUserRegister bookStoreUserRegister) {
        this.authorService = authorService;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("authorsMap")
    public Map<String,List<Author>> authorsMap(){
        return authorService.getAuthorsMap();
    }

    @GetMapping("/authors")
    public String authorsPage(){
        return "/authors/index";
    }

    @GetMapping("/authors/{authorsName}")
    public String authorsSlugPage(@PathVariable(value = "authorsName", required = true) String authorsName, Model model){
        model.addAttribute("author", authorService.getAuthorByName(authorsName));
        model.addAttribute("books", authorService.getBooksByAuthor(authorsName, 0, 6));
        return "/authors/slug";
    }

    @GetMapping("api/authors")
    @ResponseBody
    public Map<String,List<Author>> authors(){
        return authorService.getAuthorsMap();
    }
}
