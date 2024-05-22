package com.example.MyBookShopApp.data.dto;

import com.example.MyBookShopApp.data.struct.book.Book;
import org.springframework.data.domain.Page;

import java.util.List;

public class BooksPageDto {

    private long count;
    private List<Book> books;

    public BooksPageDto(Page<Book> books) {
        this.books = books.getContent();
        this.count = books.getTotalElements();
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
