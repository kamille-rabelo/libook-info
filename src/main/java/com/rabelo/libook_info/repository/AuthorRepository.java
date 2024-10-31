package com.rabelo.libook_info.repository;

import com.rabelo.libook_info.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String name);
}
