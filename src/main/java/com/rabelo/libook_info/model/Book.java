package com.rabelo.libook_info.model;

import com.rabelo.libook_info.enumeration.Language;
import com.rabelo.libook_info.model.dto.BookDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "authors_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();
    @Enumerated(value = EnumType.STRING)
    private Language language;
    private Long totalDownloads;

    public Book() {}

    public Book(BookDTO book) {
        this.title = book.title();
        try {
            this.language = Language.valueOf(book.languages().getFirst().toUpperCase());
        } catch (IllegalArgumentException e) {
            this.language = Language.OTHER;
        }
        this.totalDownloads = book.totalDownloads();
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Author(s): " + authors + "\n" +
                "Language: " + language + "\n" +
                "Downloads: " + totalDownloads + "\n";
    }
}
