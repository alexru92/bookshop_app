package com.example.MyBookShopApp.security.jwt;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expired_tokens")
public class ExpiredTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String token;

    @Column(columnDefinition = "DATE NOT NULL")
    private LocalDateTime time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
