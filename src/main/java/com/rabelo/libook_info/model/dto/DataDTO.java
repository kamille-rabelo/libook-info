package com.rabelo.libook_info.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties
public record DataDTO(List<BookDTO> results) {
}
