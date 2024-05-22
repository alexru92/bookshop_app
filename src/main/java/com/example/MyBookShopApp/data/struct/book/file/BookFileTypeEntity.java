package com.example.MyBookShopApp.data.struct.book.file;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book_file_type")
public class BookFileTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "bookFileType")
    List<BookFileEntity> bookFileEntities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BookFileEntity> getBookFileEntities() {
        return bookFileEntities;
    }

    public void setBookFileEntities(List<BookFileEntity> bookFileEntities) {
        this.bookFileEntities = bookFileEntities;
    }
}
