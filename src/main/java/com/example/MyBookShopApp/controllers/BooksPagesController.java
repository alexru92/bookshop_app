package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksPagesController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final ResourceStorage storage;
    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public BooksPagesController(BookService bookService, AuthorService authorService, ResourceStorage storage, BookStoreUserRegister bookStoreUserRegister) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.storage = storage;
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @ModelAttribute("curUsr")
    public UserEntity curUsr() {
        return bookStoreUserRegister.getCurrentUser();
    }

    @ModelAttribute("books")
    public List<Book> books(){
        return new ArrayList<>();
    }


    @GetMapping("/recent")
    public String recentPage(Model model) throws ParseException {
        model.addAttribute("books", bookService.getPageOfRecentBooks(0, 20, null, null).getContent());
        return "/books/recent";
    }

    @GetMapping("/popular")
    public String popularPage(Model model){
        model.addAttribute("books", bookService.getPageOfPopularBooks(0, 20).getContent());
        return "/books/popular";
    }

    @GetMapping("/author/{authorsName}")
    public String authorPage(@PathVariable(value = "authorsName", required = true) String authorsName, Model model){
        model.addAttribute("author", authorService.getAuthorByName(authorsName));
        model.addAttribute("books", authorService.getBooksByAuthor(authorsName, 0, 20));
        return "/books/author";
    }

    @GetMapping("/{slug}")
    public String exactBookPage(@PathVariable(value = "slug") String slug, Model model){
        model.addAttribute("exactBook", bookService.getBookBySlug(slug));
        model.addAttribute("bookRating", bookService.getBookRating(slug));
        model.addAttribute("bookReviews", bookService.findBookReviewsByBookSlug(slug, 0, 4));
        if(model.getAttribute("curUsr") != null) {
            return "/books/slugmy";
        } else {
            return "/books/slug";
        }
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug")String slug) throws IOException {
        bookService.saveBookImage(file, slug);
        return "redirect:/books/" + slug;
    }

    @GetMapping("download/{hash}")
    public ResponseEntity<ByteArrayResource> downloadBook(@PathVariable(value = "hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: "+path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: "+mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: "+data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
