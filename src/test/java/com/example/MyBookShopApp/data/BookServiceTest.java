package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.CommentWithAutorAndRatingDto;
import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    private final BookService bookService;

    @MockBean
    private BookRepository bookRepositoryMock;

    @MockBean
    private BookReviewRepository bookReviewRepository;

    @Autowired
    BookServiceTest(BookService bookService) {
        this.bookService = bookService;
    }

    //there was no task to implement a business logic for recommended books yet. Therefore .findAll() method should be
    //called by bookRepository and this test is checking that fact
    @Test
    void getPageOfRecommendedBooks() {
        int offset = 0;
        int limit = 6;
        Mockito
                .when(bookRepositoryMock.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Page<Book> books = bookService.getPageOfRecommendedBooks(offset, limit);
        assertNotNull(books);

        Mockito.verify(bookRepositoryMock, Mockito.times(1))
                .findAll(Mockito.any(Pageable.class));
    }

    @Test
    void getPageOfPopularBooks() {
        int offset = 0;
        int limit = 6;
        Mockito
                .when(bookRepositoryMock.findAllBooksByPopularity(Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Page<Book> books = bookService.getPageOfPopularBooks(offset, limit);
        assertNotNull(books);

        Mockito.verify(bookRepositoryMock, Mockito.times(1))
                .findAllBooksByPopularity(Mockito.any(Pageable.class));
    }

    @Test
    void findBookReviewsByBookSlug() {
        ArrayList<CommentWithAutorAndRatingDto> comments = new ArrayList<CommentWithAutorAndRatingDto>();
        comments.add(new CommentWithAutorAndRatingDto(1, "text", LocalDateTime.now(), new UserEntity(), 1));
        PageImpl<CommentWithAutorAndRatingDto> commentsPage = new PageImpl<CommentWithAutorAndRatingDto>(comments);

        Mockito
                .when(bookRepositoryMock.findBookBySlug("slug"))
                .thenReturn(new Book());
        Mockito
                .when(bookReviewRepository.findBookReviewsByBookSlug(Mockito.any(Book.class), Mockito.any(PageRequest.class)))
                .thenReturn(commentsPage);
        Mockito
                .when(bookReviewRepository.findLikesOrDislikes(Mockito.any(Integer.class), Mockito.any(Short.class)))
                .thenReturn(Mockito.any(Integer.class));



        assertNotNull(bookService.findBookReviewsByBookSlug("slug", 0, 4));

        Mockito.verify(bookRepositoryMock, Mockito.times(1))
                .findBookBySlug(Mockito.any(String.class));
        Mockito.verify(bookReviewRepository, Mockito.times(1))
                .findBookReviewsByBookSlug(Mockito.any(Book.class), Mockito.any(Pageable.class));
        Mockito.verify(bookReviewRepository, Mockito.times(2))
                .findLikesOrDislikes(Mockito.any(Integer.class), Mockito.any(Short.class));
    }
}