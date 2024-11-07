package com.rabelo.libook_info.repository;

import com.rabelo.libook_info.dto.BookLanguageStatisticsDTO;
import com.rabelo.libook_info.enumeration.Language;
import com.rabelo.libook_info.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);

    List<Book> findByTitleContainingIgnoreCaseOrderByTitle(String title);

    List<Book> findByLanguage(Language language);

    @Query("SELECT b.language AS language, COUNT(b.title) AS count, SUM(b.totalDownloads) AS downloads" +
            " FROM Book b GROUP BY b.language ORDER BY COUNT(b.title) DESC")
    List<BookLanguageStatisticsDTO> listStatsByLanguage();

    List<Book> findTop10ByOrderByTotalDownloadsDesc();

    List<Book> findAllByOrderByTitle();
}
