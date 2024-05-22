package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.CommentWithAutorAndRatingDto;
import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.book.review.BookReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {

    @Query("SELECT " +
            "new com.example.MyBookShopApp.data.dto.CommentWithAutorAndRatingDto(bR.id, bR.text, bR.time, bR.user, rt.rating) " +
            "FROM BookReviewEntity bR " +
            "LEFT JOIN RatingEntity rt ON rt.user = bR.user AND bR.book=rt.book " +
            "WHERE bR.book = :book " +
            "ORDER BY bR.time")
    Page<CommentWithAutorAndRatingDto> findBookReviewsByBookSlug(Book book, Pageable nextPage);

    @Query("SELECT COUNT(bRL.value) FROM BookReviewLikeEntity bRL WHERE bRL.reviewId = :id AND bRL.value = :val")
    Integer findLikesOrDislikes(Integer id, Short val);
}
