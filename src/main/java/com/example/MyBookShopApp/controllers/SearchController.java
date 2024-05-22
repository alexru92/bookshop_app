package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.customErrors.EmptySearchException;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private final BookService bookService;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public SearchController(BookService bookService, BookStoreUserRegister bookStoreUserRegister) { this.bookService = bookService;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }

    @ModelAttribute("searchSize")
    public Long searchSize(){
        return 0L;
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults(){
        return new ArrayList<>();
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                   Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            if (searchWordDto != null) {
                Page<Book> booksPage = bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 20);
                model.addAttribute("searchResults", booksPage.getContent());
                model.addAttribute("searchSize", booksPage.getTotalElements());
            }
            return "/search/index";
        } else {
            throw new EmptySearchException("This field should not be empty");
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit));
    }
}

