package com.library.management.book_service.controller;

import com.library.management.book_service.dto.AuthorDto;
import com.library.management.book_service.service.AuthorService;
import com.library.management.book_service.util.AuthorNotFoundException;
import com.library.management.book_service.util.DuplicateAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/fetchAuthor")
    public ResponseEntity<AuthorDto> fetchAuthor(@RequestParam("authorId") int authorId) {
        try {
            Optional<AuthorDto> authorDto = authorService.fetchAuthor(authorId);
            return authorDto.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (AuthorNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/addAuthor")
    public ResponseEntity<Void> addAuthor(@RequestBody AuthorDto authorDto){
        try {
            authorService.addAuthor(authorDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch(DuplicateAuthorException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
