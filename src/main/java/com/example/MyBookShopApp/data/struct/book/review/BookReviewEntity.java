package com.example.MyBookShopApp.data.struct.book.review;

import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "book_review")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
