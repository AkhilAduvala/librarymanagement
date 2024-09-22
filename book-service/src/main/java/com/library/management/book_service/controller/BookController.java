package com.library.management.book_service.controller;

import com.library.management.book_service.dto.BookDto;
import com.library.management.book_service.service.BookService;
import com.library.management.book_service.util.AuthorNotFoundException;
import com.library.management.book_service.util.BookAlreadyExists;
import com.library.management.book_service.util.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/fetchBook")
    public ResponseEntity<BookDto> fetchBook(@RequestParam("bookName") String bookName){
        System.out.println("fetch");
        try {
            BookDto bookDto = bookService.fetchBook(bookName);
            return ResponseEntity.ok(bookDto);
        } catch (BookNotFoundException e) {
            //throw new RuntimeException(e);
            //logger.error("Book not found: {}", bookName, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/addBook")
    public ResponseEntity<Void> addBook(@RequestBody BookDto bookDto){
        System.out.println("add");
        try{
            bookService.addBook(bookDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (AuthorNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (BookAlreadyExists e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateBook")
    public ResponseEntity<Void> updateBook(@RequestBody BookDto bookDto){
        try {
            bookService.updateBook(bookDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (BookNotFoundException | AuthorNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteBook")
    public ResponseEntity<BookDto> deleteBook(@RequestParam("bookName") String bookName,
                                           @RequestParam("authorId") int authorId){
        try {
            BookDto bookDto = bookService.deleteBook(bookName, authorId);
            return ResponseEntity.ok(bookDto);
        }catch (BookNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/test")
    public void test(){
        System.out.println("test");
    }
}
