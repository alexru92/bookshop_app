package com.example.MyBookShopApp.data.struct.book.links;

import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.tag.Tag;
import javax.persistence.*;

@Entity
@Table(name = "book2tag")
public class Book2TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "books", referencedColumnName = "id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "tags", referencedColumnName = "id")
    private Tag tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}

