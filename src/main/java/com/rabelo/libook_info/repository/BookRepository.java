package com.rabelo.libook_info.repository;

import com.rabelo.libook_info.dto.LanguageBookStatsDTO;
import com.rabelo.libook_info.enumeration.Language;
import com.rabelo.libook_info.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name ILIKE %:toSearch% OR b.title ILIKE %:toSearch% ORDER BY b.title")
    List<Book> searchByTitleOrAuthorsName(String toSearch);

    List<Book> findByLanguage(Language language);
}
