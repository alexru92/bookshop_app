package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.struct.author.Author;
import com.example.MyBookShopApp.data.struct.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = authorRepository.findAll();
        return authors
                .stream()
                .collect(
                        Collectors.groupingBy(
                            (Author a) ->
                                    a.getName().substring(0,1)
                        )
                );
    }

    public Author getAuthorByName(String authorsName){
        return authorRepository.getAuthorByName(authorsName).get(0);
    }

    public Page<Book> getBooksByAuthor(String authorName, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return authorRepository.findBooksByAuthorName(authorName, nextPage);
    }
}
