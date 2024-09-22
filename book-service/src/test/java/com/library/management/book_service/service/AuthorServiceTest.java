package com.library.management.book_service.service;

import com.library.management.book_service.dto.AuthorDto;
import com.library.management.book_service.model.Author;
import com.library.management.book_service.repository.AuthorRepository;
import com.library.management.book_service.util.AuthorNotFoundException;
import com.library.management.book_service.util.DuplicateAuthorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp(){
        openMocks(this);
    }

    @Test
    void shouldReturnAuthorDtoIfAuthorExists() throws AuthorNotFoundException {
        int authorId = 1;

        Author author = new Author();

        author.setAuthorName("Akhil");
        author.setAuthorId(1);

        when(authorRepository.findByAuthorId(authorId)).thenReturn(author);

        Optional<AuthorDto> authorDto = authorService.fetchAuthor(authorId);

        assertEquals("Akhil", authorDto.get().getAuthorName());
        assertEquals(1, authorDto.get().getAuthorId());

        verify(authorRepository, times(1)).findByAuthorId(authorId);
    }

    @Test
    void shouldReturnNullIfAuthorDoesNotExists() throws AuthorNotFoundException {
        int authorId = 99;

        when(authorRepository.findByAuthorId(99)).thenReturn(null);

        Optional<AuthorDto> authorDto = authorService.fetchAuthor(99);

        assertFalse(authorDto.isPresent());
        verify(authorRepository, times(1)).findByAuthorId(99);
    }

    @Test
    void shouldBeAbleToAddAAuthor() throws DuplicateAuthorException {

        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorName("Alex");

        Author author = new Author();

        author.setAuthorName("Alex");

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        authorService.addAuthor(authorDto);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void shouldReturnDuplicateAuthorExceptionWhenAuthorExists() throws DuplicateAuthorException {

        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorName("Akhil");

        when(authorRepository.existsByAuthorName(authorDto.getAuthorName())).thenReturn(true);

        assertThrows(DuplicateAuthorException.class, () -> authorService.addAuthor(authorDto));

        verify(authorRepository, times(1)).existsByAuthorName(authorDto.getAuthorName());
        verify(authorRepository, times(0)).save(new Author());
    }
}
