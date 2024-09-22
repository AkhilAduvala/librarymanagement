package com.library.management.book_service.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
public class BookDto {

    private  int bookId;

    private String bookName;

    private int authorId;

    private int publishedYear;

    private int totalQty;

    private  int availableQty;

    private String status;

    public enum Status{
        Available,
        Archive
    }

}
