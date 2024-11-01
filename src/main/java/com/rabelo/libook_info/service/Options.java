package com.rabelo.libook_info.service;

import com.rabelo.libook_info.model.Author;
import com.rabelo.libook_info.model.Book;
import com.rabelo.libook_info.model.dto.DataDTO;
import com.rabelo.libook_info.repository.AuthorRepository;
import com.rabelo.libook_info.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static com.rabelo.libook_info.service.HttpClient.getData;

@Service
public class Options {

    private final Scanner scanner = new Scanner(System.in);
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public void searchBookByTitleAndAuthorsName() {
        System.out.println("Enter a book by title:");
        var title = scanner.nextLine().trim();
        getDataFromApi(title);
        var books = bookRepository.searchByTitleOrAuthorsName(title);
        System.out.println("BOOKS FOUND:");
        books.forEach(System.out::println);
        System.out.println();
    }

    private void getDataFromApi(String title) {
        var data = getData(URLEncoder.encode(title, StandardCharsets.UTF_8), DataDTO.class);
        System.out.println(data.results());
        data.results().stream()
                .filter(b -> !bookRepository.existsByTitle(b.title()))
                .forEach(b -> {
                    var book = new Book(b);
                    if (!b.authors().isEmpty()) {
                        b.authors().stream()
                                .filter(a -> !authorRepository.existsByName(a.name()))
                                .map(a -> new Author(a, book))
                                .forEach(a -> book.getAuthors().add(a));
                    }
                    bookRepository.save(book);
                });
    }

    public void listBooksRegistered() {
        System.out.println("\nBOOKS REGISTERED:\n");
        bookRepository.findAll().forEach(System.out::println);
        System.out.println();
    }
}
