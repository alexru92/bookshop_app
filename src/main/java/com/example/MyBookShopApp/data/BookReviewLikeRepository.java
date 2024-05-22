package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLikeEntity, Integer> {

    BookReviewLikeEntity findBookReviewLikeEntityByReviewIdAndUserId(int reviewId, Integer userId);
}
