package com.rabelo.libook_info.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties
public record DataDTO(List<BookDTO> results) {
}
