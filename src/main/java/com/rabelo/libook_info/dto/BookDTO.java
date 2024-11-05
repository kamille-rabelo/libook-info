package com.rabelo.libook_info.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Set;

@JsonIgnoreProperties
public record BookDTO(String title,
                      Set<AuthorDTO> authors,
                      List<String> languages,
                      @JsonAlias("download_count") Long totalDownloads) {
}
