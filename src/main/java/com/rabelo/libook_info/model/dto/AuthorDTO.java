package com.rabelo.libook_info.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.Year;

public record AuthorDTO(String name,
                        @JsonAlias("birth_year") Year birthYear,
                        @JsonAlias("death_year") Year deathYear) {
}
