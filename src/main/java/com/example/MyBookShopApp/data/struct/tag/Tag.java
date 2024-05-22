package com.example.MyBookShopApp.data.struct.tag;

import com.example.MyBookShopApp.data.struct.book.links.Book2TagEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String tagName;

    @OneToMany(mappedBy = "tag")
    @JsonIgnore
    private List<Book2TagEntity> book2TagEntities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<Book2TagEntity> getBook2TagEntities() {
        return book2TagEntities;
    }

    public void setBook2TagEntities(List<Book2TagEntity> book2TagEntities) {
        this.book2TagEntities = book2TagEntities;
    }
}
