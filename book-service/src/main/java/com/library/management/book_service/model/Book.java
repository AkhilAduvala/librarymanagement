package com.library.management.book_service.model;

import com.library.management.book_service.dto.BookDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Year;
import java.util.Date;

@Entity
@Table(name = "Books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int bookId;

    private String bookName;

    private int publishedYear;

    private int totalQty;

    private  int availableQty;

    private String status;

    @ManyToOne
    @JoinColumn(name = "authorId")
    private Author author;

    @PrePersist
    protected void onCreate(){
        if (status == null){
            status = String.valueOf(Book.Status.Available);
        }
    }

    public enum Status{
        Available,
        Archive
    }
}
