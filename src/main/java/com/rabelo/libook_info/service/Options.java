package com.rabelo.libook_info.service;

import com.rabelo.libook_info.dto.DataDTO;
import com.rabelo.libook_info.enumeration.Language;
import com.rabelo.libook_info.model.Author;
import com.rabelo.libook_info.model.Book;
import com.rabelo.libook_info.repository.AuthorRepository;
import com.rabelo.libook_info.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.DoubleSummaryStatistics;
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

    public void searchBooksByTitle() {
        System.out.println("Enter a book title:");
        var title = scanner.nextLine().trim();
        getDataFromApi(title);

        var books = bookRepository.findByTitleContainingIgnoreCaseOrderByTitle(title);
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
        printList("BOOKS REGISTERED:", bookRepository.findAllByOrderByTitle());
    }

    public void listAuthorsRegistered() {
        printList("AUTHORS REGISTERED:", authorRepository.findAllByOrderByName());
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

    public void searchAuthorsAliveInYear() {
        System.out.println("Enter a year in the format (YYYY):");
        try {
            var year = Year.parse(scanner.nextLine().trim());
            printList("AUTHORS ALIVE IN " + year + ":", authorRepository.searchAuthorsAliveInYear(year));

        } catch (DateTimeParseException e) {
            System.err.println("Error: Value not in format (YYYY) :(");
        }
    }

    public void searchBooksByLanguage() {
        System.out.println("Type in an ISO (e.g. es):");
        Language.printLanguages();
        var iso = scanner.nextLine().trim();

        try {
            var language = Language.valueOf(iso.toUpperCase());
            var booksFound = bookRepository.findByLanguage(language);
            printList("BOOKS IN " + language.getLanguage().toUpperCase() + " (" + booksFound.size() +") :", booksFound);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: Value isn't an ISO :(");
        }
    }

    public void listBookLanguageStatistics() {
        var stats = bookRepository.listStatsByLanguage();
        System.out.println("\nBOOK COUNT BY LANGUAGE:\n");

        stats.forEach(s -> System.out.printf("Books in %s: %d - number of downloads: %.2f - average: %.2f\n",
                s.getLanguage(), s.getCount(), s.getDownloads(), s.getDownloads() / s.getCount()));
        System.out.println();
    }

    public void listTop10MostDownloadedBooks() {
        var top10 = bookRepository.findTop10ByOrderByTotalDownloadsDesc();
        printList("TOP 10 MOST DOWNLOADED BOOKS:", top10);
    }

    public void searchAuthorsByName() {
        System.out.println("Enter an author's name:");
        var authorName = scanner.nextLine().trim();
        var authors = authorRepository.findByNameContainingIgnoreCaseOrderByName(authorName);

        if (!authors.isEmpty()) {
            System.out.println("\nRESULTS FOR \"" + authorName.toUpperCase() + "\":\n");
            authors.forEach(a -> {
                var books = a.getBooks();
                printAuthorStatistics(a, books);
                printList("BOOKS:", books);
            });
        } else {
            System.out.println("No results for " + authorName + " :(");
        }
    }

    private void printAuthorStatistics(Author a, List<Book> books) {
        var stats = new DoubleSummaryStatistics();
        System.out.println("AUTHOR: " + a);
        books.forEach(b -> stats.accept(b.getTotalDownloads()));

        System.out.printf("""
                                Number of books: %d
                                Average downloads: %.2f
                                Total downloads: %.2f
                                Most popular: %.2f
                                """,
                stats.getCount(), stats.getAverage(), stats.getSum(), stats.getMax());
    }
}
