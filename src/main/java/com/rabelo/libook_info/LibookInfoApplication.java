package com.rabelo.libook_info;

import com.rabelo.libook_info.model.Author;
import com.rabelo.libook_info.model.Book;
import com.rabelo.libook_info.model.dto.DataDTO;
import com.rabelo.libook_info.repository.AuthorRepository;
import com.rabelo.libook_info.repository.BookRepository;
import com.rabelo.libook_info.service.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibookInfoApplication implements CommandLineRunner {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LibookInfoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var client = new HttpClient();
		var data = client.getData("da+vinci", DataDTO.class);
		System.out.println(data.results());
        data.results().stream()
                .filter(b -> !bookRepository.existsByTitle(b.title()))
                .forEach(b -> {
                    var book = new Book(b);
                    if (!b.authors().isEmpty()) {
                        b.authors().stream()
                                .filter(a -> authorRepository.existsByName(a.name()))
                                .map(a -> new Author(a, book))
                                .forEach(a -> book.getAuthors().add(a));
                    }
                });
		var books = bookRepository.searchByTitleOrAuthorsName("da vinci");

		books.forEach(System.out::println);
        System.out.println();
		bookRepository.findAll().forEach(System.out::println);
		System.out.println();
		authorRepository.findAll().forEach(System.out::println);
	}
}
