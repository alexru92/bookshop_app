package com.example.MyBookShopApp.data.struct.book.file;

import com.example.MyBookShopApp.data.struct.book.Book;
import javax.persistence.*;

@Entity
@Table(name = "book_file")
public class BookFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private BookFileTypeEntity bookFileType;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookFileTypeEntity getBookFileType() {
        return bookFileType;
    }

    public void setBookFileType(BookFileTypeEntity bookFileType) {
        this.bookFileType = bookFileType;
    }

    public String returnStringOfType(){
        return this.bookFileType.getName();
    }
}
