package com.example.MyBookShopApp.data.struct.book;
import com.example.MyBookShopApp.data.struct.book.file.BookFileEntity;
import com.example.MyBookShopApp.data.struct.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.data.struct.book.links.Book2GenreEntity;
import com.example.MyBookShopApp.data.struct.book.links.Book2TagEntity;
import com.example.MyBookShopApp.data.struct.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.struct.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.struct.other.RatingEntity;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2AuthorEntity> book2AuthorEntities;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2UserEntity> book2UserEntities;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2TagEntity> book2TagEntities;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2GenreEntity> book2GenreEntities;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookFileEntity> bookFileEntities;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<RatingEntity> ratingEntities;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookReviewEntity> bookReviewEntities;

    @Column(columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    @Column(columnDefinition = "BOOLEAN NOT NULL")
    private Boolean isBestseller;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @Column(columnDefinition = "VARCHAR(255)")
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer price;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private Integer discount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Boolean getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(Boolean isBestseller) {
        this.isBestseller = isBestseller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public List<Book2AuthorEntity> getBook2AuthorEntities() {
        return book2AuthorEntities;
    }

    public void setBook2AuthorEntities(List<Book2AuthorEntity> book2AuthorEntities) {
        this.book2AuthorEntities = book2AuthorEntities;
    }

    public List<Book2UserEntity> getBook2UserEntities() {
        return book2UserEntities;
    }

    public void setBook2UserEntities(List<Book2UserEntity> book2UserEntities) {
        this.book2UserEntities = book2UserEntities;
    }

    public List<Book2TagEntity> getBook2TagEntities() {
        return book2TagEntities;
    }

    public void setBook2TagEntities(List<Book2TagEntity> book2TagEntities) {
        this.book2TagEntities = book2TagEntities;
    }

    public List<Book2GenreEntity> getBook2GenreEntities() {
        return book2GenreEntities;
    }

    public void setBook2GenreEntities(List<Book2GenreEntity> book2GenreEntities) {
        this.book2GenreEntities = book2GenreEntities;
    }

    public List<BookFileEntity> getBookFileEntities() {
        return bookFileEntities;
    }

    public void setBookFileEntities(List<BookFileEntity> bookFileEntities) {
        this.bookFileEntities = bookFileEntities;
    }

    public List<RatingEntity> getRatingEntities() {
        return ratingEntities;
    }

    public void setRatingEntities(List<RatingEntity> ratingEntities) {
        this.ratingEntities = ratingEntities;
    }

    public List<BookReviewEntity> getBookReviewEntities() {
        return bookReviewEntities;
    }

    public void setBookReviewEntities(List<BookReviewEntity> bookReviewEntities) {
        this.bookReviewEntities = bookReviewEntities;
    }

    @JsonGetter("authors")
    public List<String> getAuthorNames(){
        List<String> authorNames = new ArrayList<>();
        for (Book2AuthorEntity book2AuthorEntity : getBook2AuthorEntities()) {
            authorNames.add(book2AuthorEntity.getAuthor().getName());
        }
        return authorNames;
    }

    public Integer getOldPrice(){
        double res = price / (1 - discount * 1.0 / 100);
        return (int) res;
    }

    public String getAuthorNamesAsString(){
        return String.join(",", getAuthorNames());
    }

    public String getFirstAuthorName(){
        return getAuthorNames().get(0);
    }
}
