package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.book.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookRepositoryTests {

    private final BookRepository bookRepository;

    @Autowired
    BookRepositoryTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Test
    void findBooksByAuthor_FirstName() {
        String token = "Alys Philipeaux";
        List<Book> bookListByAuthorFirstName = bookRepository.findBooksByAuthorName(token);

        assertNotNull(bookListByAuthorFirstName);
        assertFalse(bookListByAuthorFirstName.isEmpty());

        for (Book book : bookListByAuthorFirstName) {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertTrue(book.getAuthorNames().contains(token));
        }
    }

    @Test
    void findBooksByTitleContaining() {
        String token = "Just";
        List<Book> bookListByTitleContaining = bookRepository.findBooksByTitleContaining(token);
        assertNotNull(bookListByTitleContaining);
        assertFalse(bookListByTitleContaining.isEmpty());

        for (Book book : bookListByTitleContaining) {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertThat(book.getTitle()).contains(token);
        }
    }

    @Test
    void getBestsellers() {
        List<Book> bestSellersBooks = bookRepository.getBestsellers();
        assertNotNull(bestSellersBooks);
        assertFalse(bestSellersBooks.isEmpty());

        assertThat(bestSellersBooks.size()).isGreaterThan(1);
    }
}