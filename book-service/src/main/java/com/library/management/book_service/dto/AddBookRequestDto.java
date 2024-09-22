package com.library.management.book_service.dto;

import lombok.Data;

@Data
public class AddBookRequestDto {

    private AuthorDto authorDto;
    private BookDto bookDto;
}
