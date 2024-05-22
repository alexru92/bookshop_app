package com.example.MyBookShopApp.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<ExpiredTokens, Integer> {

    @Query("FROM ExpiredTokens et WHERE et.time >= :time")
    List<ExpiredTokens> findAllNotOlderThanTime(LocalDateTime time);
}
