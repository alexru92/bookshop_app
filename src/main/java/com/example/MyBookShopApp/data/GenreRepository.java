package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.dto.TagRate;
import com.example.MyBookShopApp.data.struct.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    @Query("FROM GenreEntity g WHERE g.parent = null")
    List<GenreEntity> getParentGenres();

    @Query ("SELECT g.name FROM GenreEntity g WHERE g.slug = :slug")
    List<String> getGenreName(String slug);

    @Query("SELECT COUNT(b2t.id) FROM Book2TagEntity b2t")
    List<Long> getTagsQuantity();

    @Query("SELECT new com.example.MyBookShopApp.data.dto.TagRate(t.tagName, COUNT(b2t.tag)) FROM Book2TagEntity b2t JOIN b2t.tag t GROUP BY t.tagName")
    List<TagRate> getTags();
}
