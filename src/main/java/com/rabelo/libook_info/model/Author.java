package com.rabelo.libook_info.model;

import com.rabelo.libook_info.dto.AuthorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Year birthYear;
    private Year deathYear;
    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Author() {}

    public Author(AuthorDTO author, Book toAdd) {
        this.name = author.name();
        this.birthYear = author.birthYear();
        this.deathYear = author.deathYear();
        this.books.add(toAdd);
    }

    @Override
    public String toString() {
        return name + " (" + birthYear + " - " + deathYear + ") ";
    }
}
