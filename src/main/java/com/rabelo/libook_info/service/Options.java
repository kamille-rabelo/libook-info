package com.rabelo.libook_info.service;

import com.rabelo.libook_info.enumeration.Language;
import com.rabelo.libook_info.model.Author;
import com.rabelo.libook_info.model.Book;
import com.rabelo.libook_info.dto.DataDTO;
import com.rabelo.libook_info.repository.AuthorRepository;
import com.rabelo.libook_info.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.List;
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
        printList("BOOKS FOUND:", books);
    }

    private void getDataFromApi(String title) {
        var data = getData(URLEncoder.encode(title, StandardCharsets.UTF_8), DataDTO.class);
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
        printList("BOOKS REGISTERED:", bookRepository.findAll());
    }

    public void listAuthorsRegistered() {
        printList("AUTHORS REGISTERED:", authorRepository.findAll());
    }

    private <T> void printList(String message, List<T> list) {
        if (!list.isEmpty()) {
            System.out.println( "\n" + message + "\n");
            list.forEach(System.out::println);
            System.out.println();
        } else {
            System.out.println("\nNo results :(\n");
        }
    }

    public void listAuthorsAliveInYear() {
        System.out.println("Enter a year in the format (YYYY):");
        try {
            var year = Year.parse(scanner.nextLine().trim());
            printList("AUTHORS ALIVE IN :" + year, authorRepository.searchAuthorsAliveInYear(year));
        } catch (DateTimeParseException e) {
            System.err.println("Error: Value not in format (YYYY) :(");
        }
    }

    public void listBooksByLanguage() {
        System.out.println("Type in an ISO (e.g. es):");
        Language.printLanguages();
        var iso = scanner.nextLine().trim();
        try {
            var language = Language.valueOf(iso.toUpperCase());
            var booksFound = bookRepository.findByLanguage(language);
            printList("BOOKS IN " + language.getLanguage().toUpperCase() + ":", booksFound);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Value isn't an ISO :(");
        }
    }
}
