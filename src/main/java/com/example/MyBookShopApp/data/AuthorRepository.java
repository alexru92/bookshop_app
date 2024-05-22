package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.author.Author;
import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.genre.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    List<Author> getAuthorByName(String authorsName);

    @Query("SELECT b FROM Book b " +
            "JOIN b.book2AuthorEntities b2a JOIN b2a.author a " +
            "WHERE a.name LIKE :authorName")
    Page<Book> findBooksByAuthorName(String authorName, Pageable nextPage);
}
