package com.example.MyBookShopApp.data.struct.genre;

import com.example.MyBookShopApp.data.struct.book.Book;
import com.example.MyBookShopApp.data.struct.book.links.Book2GenreEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "genre")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private GenreEntity parent;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="parent", orphanRemoval = true)
    private List<GenreEntity> children;

    @OneToMany(mappedBy = "genre")
    @JsonIgnore
    private List<Book2GenreEntity> book2GenreEntities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GenreEntity getParent() {
        return parent;
    }

    public void setParent(GenreEntity parent) {
        this.parent = parent;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GenreEntity> getChildren() {
        return children;
    }

    public void setChildren(List<GenreEntity> children) {
        this.children = children;
    }

    public List<Book2GenreEntity> getBook2GenreEntities() {
        return book2GenreEntities;
    }

    public void setBook2GenreEntities(List<Book2GenreEntity> book2GenreEntities) {
        this.book2GenreEntities = book2GenreEntities;
    }

    public int retrieveRate() {
        return book2GenreEntities.size() + children.stream().mapToInt(GenreEntity::retrieveRate).sum();
    }

//    public List<Book> retrieveBooks() {
//        List<Book> thisGenreBooks = book2GenreEntities.stream()
//               .map(Book2GenreEntity::getBook)
//               .collect(Collectors.toList());
//        List<Book> childGenresBooks = children.stream()
//               .map(GenreEntity::retrieveBooks)
//               .flatMap(Collection::stream)
//               .collect(Collectors.toList());
//
//        return Stream.concat(thisGenreBooks.stream(), childGenresBooks.stream()).collect(Collectors.toList());
//    }
}
