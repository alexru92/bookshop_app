package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b " +
            "JOIN b.book2AuthorEntities b2a JOIN b2a.author a " +
            "WHERE a.name LIKE %:name%")
    List<Book> findBooksByAuthorName(@Param("name") String authorName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByDiscountBetween(Integer min, Integer max);

    Book findBookBySlug(String slug);

    @Query("FROM Book WHERE isBestseller = true")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    @Query("SELECT b FROM Book b WHERE b.pubDate >= :dateFrom AND b.pubDate <= :dateTo ORDER BY b.pubDate")
    Page<Book> findAllBooksBetweenDate(@Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo, Pageable pageable);

    @Query(value =
            "SELECT books.* FROM " +
            "   (SELECT sum(score) rate, id FROM " +
            "       (SELECT CASE " +
            "           WHEN type_id = 1  THEN count(type_id) " +
            "           WHEN type_id = 2  THEN 0.7 * count(type_id) " +
            "           WHEN type_id = 3  THEN 0.4 * count(type_id) " +
            "           ELSE 0 " +
            "       END score, type_id, books.* " +
            "       FROM book2user " +
            "       RIGHT JOIN books ON book2user.book_id = books.id " +
            "       GROUP BY books.id , type_id) rates_and_types " +
            "   GROUP BY id) popularity " +
            "JOIN books ON popularity.id = books.id " +
            "ORDER BY popularity.rate DESC, books.title ASC",
            countQuery = "SELECT count(id) FROM books",
            nativeQuery = true)
    //The last nested SELECT chooses all books and their statuses (was it bought, stored or put in the user basket
    //and how many times). Also within this SELECT the got number multiplies by a number specified in requirements.
    //JOIN within this SELECT is used to get all books, that may absent in book2user table.
    //Within the medium SELECT we summarize scores to get one rate for each book
    //Within the main SELECT we are joining the got table with books by book_id and select all rows for book,
    //ordering it by rate
    Page<Book> findAllBooksByPopularity(Pageable nextPage);

    @Query ("SELECT b FROM Book b JOIN b.book2TagEntities b2a JOIN b2a.tag t WHERE t.tagName = :tagName")
    Page<Book> getBooksByTag(String tagName, Pageable nextPage);

    @Query(value =
            "SELECT b.* FROM books b "+
            "JOIN book2genre b2g ON b.id = b2g.book_id "+
            "WHERE b2g.genre_id IN "+
            "	(WITH RECURSIVE subordinates AS ( "+
            "		SELECT id FROM genre WHERE name = :name "+
            "		UNION "+
            "		SELECT g.id FROM genre g "+
            "		JOIN subordinates s ON g.parent_id = s.id "+
            "	) SELECT id FROM subordinates)",
            nativeQuery = true)
    //recursively get the genre itself and all its children (till the end of the 'tree') and put it to WHERE condition
    Page<Book> findAllBooksByGenreWithChildren(String name, Pageable nextPage);

    @Query ("SELECT b FROM Book b WHERE b.slug IN (:cookieSlugs)")
    List<Book> findBooksBySlugIn(List<String> cookieSlugs);
}
