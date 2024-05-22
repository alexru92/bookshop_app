package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.other.RatingEntity;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

    @Query(value = "SELECT ROUND(AVG(rating), 0) FROM rating WHERE book_id = :bookId", nativeQuery = true)
    Integer getBookCommonRating(Integer bookId);

    @Query("SELECT COUNT(rat.rating) FROM RatingEntity rat WHERE rat.book.id = :bookId")
    Integer getNumberOfGrades(Integer bookId);

    @Query("SELECT COUNT(rat.rating) FROM RatingEntity rat WHERE rat.book.id = :bookId AND rat.rating = :grade")
    Integer getGradeCountByBook(Integer bookId, int grade);

    RatingEntity findRatingEntityByUserAndAndBook(UserEntity user, Book book);
}
