package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.struct.genre.GenreEntity;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GenresController {

    private final BookService bookService;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public GenresController(BookService bookService, BookStoreUserRegister bookStoreUserRegister) {
        this.bookService = bookService;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("genres")
    public List<GenreEntity> genres(){
        return bookService.getGenres();
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @GetMapping("/genres")
    public String genresPage(){
                return "/genres/index";
            }


    @GetMapping("/genres/{genreCode}")
    public String slugGenrePage(@PathVariable(value = "genreCode", required = false) String genreCode, Model model) {
        model.addAttribute("genreCode", genreCode);
        if (genreCode != null) {
            //Page<Book> booksPage = bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 20);
            model.addAttribute("books", bookService.getBooksByGenreCode(genreCode, 0, 20).getContent());
            model.addAttribute("tagName", bookService.getGenreName(genreCode));
        }
        return "/genres/slug";
    }
}
