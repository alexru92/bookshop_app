package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.customErrors.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.data.dto.CommentWithAutorAndRatingDto;
import com.example.MyBookShopApp.data.dto.TagRate;
import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.struct.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.struct.genre.GenreEntity;
import com.example.MyBookShopApp.data.struct.other.RatingEntity;
import com.example.MyBookShopApp.data.struct.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final ResourceStorage storage;
    private final GenreRepository genreRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;


    @Autowired
    public BookService(BookRepository bookRepository, RatingRepository ratingRepository, ResourceStorage storage, GenreRepository genreRepository, BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository) {
        this.bookRepository = bookRepository;
        this.ratingRepository = ratingRepository;
        this.storage = storage;
        this.genreRepository = genreRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
    }

    //NEW BOOK SEVICE METHODS

    public List<Book> getBooksByAuthor(String authorName){
        return bookRepository.findBooksByAuthorName(authorName);
    }

    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if(title.length() <= 1){
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        }else{
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if(data.size()>0){
                return data;
            }else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max){
        return bookRepository.findBooksByDiscountBetween(min,max);
    }

    public List<Book> getBooksWithMaxPrice(){
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers(){
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfRecentBooks(Integer offset, Integer limit, String from, String to) throws ParseException {
        Date dateFrom;
        Date dateTo;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

        if (from == null) {
            dateFrom = new Date(-70000000000000L);
        } else {
            dateFrom = formatter.parse(from);
        }
        if (to == null) {
            dateTo = new Date(System.currentTimeMillis());
        } else {
            dateTo = formatter.parse(to);
        }

        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAllBooksBetweenDate(dateFrom, dateTo, nextPage);
    }

    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAllBooksByPopularity(nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    public HashMap<String, String> getTags(){
        Long quantity = genreRepository.getTagsQuantity().get(0);
        List<TagRate> tags = genreRepository.getTags();
        HashMap<String, String> tagsWithFrontEndClass = new HashMap<>();
        for (TagRate tag: tags) {
            tagsWithFrontEndClass.put(tag.getTagName(), tag.getFrontEndClass(quantity));
        }
        return tagsWithFrontEndClass;
    }

    public Page<Book> getBooksByTag(Integer offset, Integer limit, String tagName){
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.getBooksByTag(tagName, nextPage);
    }

    public Book getBookBySlug(String slug){
        return bookRepository.findBookBySlug(slug);
    }

    public List<GenreEntity> getGenres() {
        return genreRepository.getParentGenres();
    }

    public Page<Book> getBooksByGenreCode(String slug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllBooksByGenreWithChildren(getGenreName(slug), nextPage);
    }

    public Page<Book> getBooksByGenreName(String name, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllBooksByGenreWithChildren(name, nextPage);
    }

    public String getGenreName(String slug) {
        return genreRepository.getGenreName(slug).get(0);
    }

    public void saveBookImage(MultipartFile file, String bookSlug) throws IOException {
        String savePath = storage.saveNewBookImage(file, bookSlug);
        Book bookToUpdate = bookRepository.findBookBySlug(bookSlug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate); //save new path in db here
    }

    public void updadeBookRating(String bookSlug, Integer rating, UserEntity user){
        Book bookToUpdate = bookRepository.findBookBySlug(bookSlug);
        RatingEntity ratingEntity = new RatingEntity(rating, bookToUpdate, user);
        RatingEntity existingRating = ratingRepository.findRatingEntityByUserAndAndBook(user, bookToUpdate);
        if (existingRating == null) {
            ratingRepository.save(ratingEntity);
        } else if (existingRating.getRating() == 0) {
            ratingRepository.delete(existingRating);
            ratingRepository.save(ratingEntity);
        }
    }

    public Map<String, Integer> getBookRating(String slug){
        Integer bookId = bookRepository.findBookBySlug(slug).getId();

        Map<String, Integer> gradesDistribution = new HashMap<>();
        for (int i = 1; i <= 5; i++){
            gradesDistribution.put("rateOf" + i, ratingRepository.getGradeCountByBook(bookId, i));
        }
        gradesDistribution.put("commonRating", ratingRepository.getBookCommonRating(bookId));
        gradesDistribution.put("numberOfGrades", ratingRepository.getNumberOfGrades(bookId));
        return gradesDistribution;
    }

    public Page<CommentWithAutorAndRatingDto> findBookReviewsByBookSlug(String slug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        Book book = bookRepository.findBookBySlug(slug);
        Page<CommentWithAutorAndRatingDto> result = bookReviewRepository.findBookReviewsByBookSlug(book, nextPage);
        if (result != null) {
            for (CommentWithAutorAndRatingDto item : result.getContent()) {
                item.setLikes(bookReviewRepository.findLikesOrDislikes(item.getId(), (short) 1));
                item.setDislikes(bookReviewRepository.findLikesOrDislikes(item.getId(), (short) -1));
            }
        }
        return result;
    }

    public void saveBookComment(String slug, String text, UserEntity user){
        Book book = bookRepository.findBookBySlug(slug);
        BookReviewEntity bookReview = new BookReviewEntity();
        bookReview.setBook(book);
        bookReview.setText(text);
        bookReview.setTime(LocalDateTime.now());
        bookReview.setUser(user);
        bookReviewRepository.save(bookReview);
        updadeBookRating(slug, 0, user);
    }

    public void saveCommentLike(Integer reviewId, Short value, UserEntity user){
        BookReviewLikeEntity existing = bookReviewLikeRepository.findBookReviewLikeEntityByReviewIdAndUserId(reviewId, user.getId());
        if (existing == null) {
            BookReviewLikeEntity bookReviewLike = new BookReviewLikeEntity();
            bookReviewLike.setReviewId(reviewId);
            bookReviewLike.setTime(LocalDateTime.now());
            bookReviewLike.setUserId(user.getId());
            bookReviewLike.setValue(value);
            bookReviewLikeRepository.save(bookReviewLike);
        }
    }
}
