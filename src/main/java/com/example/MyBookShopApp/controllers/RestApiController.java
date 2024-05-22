package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.customErrors.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.data.ApiResponse;
import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.struct.book.Book;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
public class RestApiController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public RestApiController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/books/by-author")
    @ApiOperation("operation to get book list of bookshop by passed author first name")
    public ResponseEntity<List<Book>> booksByAuthor(@RequestParam("author") String authorName){
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
    }

    @GetMapping("/books/by-title")
    @ApiOperation("get books by book title")
    public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title")String title) throws BookstoreApiWrongParameterException {
        ApiResponse<Book> response = new ApiResponse<>();
        List<Book> data = bookService.getBooksByTitle(title);
        response.setDebugMessage("successful request");
        response.setMessage("data size: "+data.size()+" elements");
        response.setStatus(HttpStatus.OK);
        response.setTimeStamp(LocalDateTime.now());
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/books/by-price-range")
    @ApiOperation("get book by price range from min price to max price")
    public ResponseEntity<List<Book>> priceRangeBooks(@RequestParam("min") Integer min, @RequestParam("max") Integer max){
        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
    }

    @GetMapping("/books/with-max-discount")
    @ApiOperation("get list of book with max price")
    public ResponseEntity<List<Book>> maxPriceBooks(){
        return ResponseEntity.ok(bookService.getBooksWithMaxPrice());
    }

    @GetMapping("/books/bestsellsers")
    @ApiOperation("get bestseller books (which is_bestseller = 1)")
    public ResponseEntity<List<Book>> bestSellerBooks(){
        return ResponseEntity.ok(bookService.getBestsellers());
    }


    @GetMapping("/books/recommended")
    public BooksPageDto getRecommended(@RequestParam(value = "offset") Integer offset,
                                       @RequestParam(value = "limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit));
    }

    @GetMapping("/books/popular")
    public BooksPageDto getPopular(@RequestParam(value = "offset") Integer offset,
                                   @RequestParam(value = "limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit));
    }

    @GetMapping("/books/recent")
    public BooksPageDto getRecent(@RequestParam(value = "from", required = false) String from,
                                  @RequestParam(value = "to", required = false) String to,
                                  @RequestParam(value = "offset") Integer offset,
                                  @RequestParam(value = "limit") Integer limit) throws ParseException {
        if(from != null) {
            if (from.equals("0")) {
                from = null;
            }
        }
        if (to != null) {
            if (to.equals("0")) {
                to = null;
            }
        }
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit, from, to));
    }


    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public BooksPageDto getApiSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        if (searchWordDto != null){
            return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 20));
        }
        return null;
    }

    @GetMapping(value = {"/books/tag", "/books/tag/{tagName}"})
    public BooksPageDto getApiTagResults(@PathVariable(value = "tagName", required = false) String tagName,
                                         @RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        if (tagName != null){
            return new BooksPageDto(bookService.getBooksByTag(offset, limit, tagName));
        }
        return null;
    }

    @GetMapping(value = {"/books/genre", "/books/genre/{genreName}"})
    public BooksPageDto getApiGenreResults(@PathVariable(value = "genreName", required = false) String genreName,
                                         @RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit) {
        if (genreName != null){
            return new BooksPageDto(bookService.getBooksByGenreName(genreName, offset, limit));
        }
        return null;
    }

    @GetMapping(value = {"/books/author/{authorName}"})
    public BooksPageDto getBooksByAuthorResults(@PathVariable(value = "authorName", required = true) String authorName,
                                                @RequestParam("offset") Integer offset,
                                                @RequestParam("limit") Integer limit) {
        if (authorName != null){
            return new BooksPageDto(authorService.getBooksByAuthor(authorName, offset, limit));
        }
        return null;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception){
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Missing required parameters",
                exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookstoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception exception){
        return new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST, "Bad parameter value...",exception)
                ,HttpStatus.BAD_REQUEST);
    }
}
