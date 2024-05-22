package com.example.MyBookShopApp.data.struct.user;

import com.example.MyBookShopApp.data.struct.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.struct.other.RatingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<RatingEntity> ratingEntities;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<BookReviewEntity> bookReviewEntities;

    @Column(columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(columnDefinition = "VARCHAR(255)")
    private String phone;

    @Column(columnDefinition = "VARCHAR(255)")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RatingEntity> getRatingEntities() {
        return ratingEntities;
    }

    public void setRatingEntities(List<RatingEntity> ratingEntities) {
        this.ratingEntities = ratingEntities;
    }

    public List<BookReviewEntity> getBookReviewEntities() {
        return bookReviewEntities;
    }

    public void setBookReviewEntities(List<BookReviewEntity> bookReviewEntities) {
        this.bookReviewEntities = bookReviewEntities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
