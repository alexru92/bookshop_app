package com.example.MyBookShopApp.data.struct.other;

import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "rating")
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    public RatingEntity(Integer rating, Book book, UserEntity user) {
        this.rating = rating;
        this.book = book;
        this.user = user;
    }

    public RatingEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
