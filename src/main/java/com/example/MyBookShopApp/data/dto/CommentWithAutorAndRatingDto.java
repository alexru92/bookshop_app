package com.example.MyBookShopApp.data.dto;

import com.example.MyBookShopApp.data.struct.user.UserEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentWithAutorAndRatingDto {

    public Integer id;

    public String comment;

    public String time;

    public UserEntity user;

    public Integer rating;

    public long likes;

    public long dislikes;

    public CommentWithAutorAndRatingDto(Integer id, String text, LocalDateTime time, UserEntity user, Integer rating) {
        this.id = id;
        this.comment = text;
        this.time = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.user = user;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }
}
