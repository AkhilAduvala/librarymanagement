package com.library.management.book_service.service;

import com.library.management.book_service.dto.AuthorDto;
import com.library.management.book_service.dto.BookDto;
import com.library.management.book_service.model.Author;
import com.library.management.book_service.model.Book;
import com.library.management.book_service.repository.BookRepository;
import com.library.management.book_service.util.AuthorNotFoundException;
import com.library.management.book_service.util.BookAlreadyExists;
import com.library.management.book_service.util.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp(){
        openMocks(this);
    }

    @Test
    void fetchBookShouldReturnBookDtoWhenBookExists() throws BookNotFoundException {
        String bookName = "GOF";

        Book book = new Book();
        Author author = new Author();

        author.setAuthorId(1);
        author.setAuthorName("GOF4");

        book.setBookName("GOF");
        book.setBookId(1);
        book.setAuthor(author);
        book.setTotalQty(10);
        book.setAvailableQty(4);
        book.setPublishedYear(1994);
        book.setStatus(String.valueOf(Book.Status.Available));

        when(bookRepository.findByBookName(bookName)).thenReturn(book);

        BookDto bookDto = bookService.fetchBook(bookName);

        assertEquals(book.getBookId(), bookDto.getBookId());
        assertEquals(book.getBookName(), bookDto.getBookName());
        assertEquals(book.getPublishedYear(), bookDto.getPublishedYear());
        assertEquals(book.getTotalQty(), bookDto.getTotalQty());
        assertEquals(book.getAvailableQty(), bookDto.getAvailableQty());
        assertEquals(book.getAuthor().getAuthorId(), bookDto.getAuthorId());
        assertEquals(book.getStatus(), String.valueOf(Book.Status.Available));

        verify(bookRepository, times(1)).findByBookName(bookName);
    }

    @Test
    void fetchBookShouldThrowExceptionWhenBookDoesNotExistsOrStatusIsArchived(){

        String bookName = "Random";

        when(bookRepository.findByBookName(bookName)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> bookService.fetchBook(bookName));

    }

    /*
    @Test
    void fetchBookShouldThrowBookNotFoundExceptionWhenBookIsArchived(){

    }
    */

    @Test
    void addBookWhenBookDoesNotExists() throws AuthorNotFoundException, BookAlreadyExists {

        //prepare authorDto
        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorId(1);
        authorDto.setAuthorName("GOF4");

        //prepare bookDto
        BookDto bookDto = new BookDto();

        bookDto.setBookId(1);
        bookDto.setBookName("GOF");
        bookDto.setAuthorId(1);
        bookDto.setPublishedYear(1994);
        bookDto.setTotalQty(10);
        bookDto.setAvailableQty(8);
        bookDto.setStatus("Available");

        //prepare author entity
        Author author = new Author();

        author.setAuthorId(authorDto.getAuthorId());
        author.setAuthorName(authorDto.getAuthorName());

        //prepare book entity
        Book book = new Book();

        //book.setBookId(bookDto.getBookId());
        book.setBookName(bookDto.getBookName());
        book.setAuthor(author);
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setTotalQty(bookDto.getTotalQty());
        book.setAvailableQty(bookDto.getAvailableQty());
        book.setStatus(String.valueOf(Book.Status.Available));

        //mock authorService to return authorDto
        when(authorService.fetchAuthor(1)).thenReturn(Optional.of(authorDto));

        when(bookRepository.existsByBookNameAndAuthor_AuthorId("GOF", 1)).thenReturn(false);

        //mock bookRepository to save and return book entity
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        //call the service method
        bookService.addBook(bookDto);

        //verify interactions
        verify(authorService, times(1)).fetchAuthor(1);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void addBookWhenAuthorDoesNotExist() throws AuthorNotFoundException {

        BookDto bookDto = new BookDto();

        bookDto.setBookId(1);
        bookDto.setBookName("GOF");
        bookDto.setAuthorId(1);
        bookDto.setPublishedYear(1994);
        bookDto.setTotalQty(10);
        bookDto.setAvailableQty(8);

        doThrow(new AuthorNotFoundException("Author details not found")).when(authorService).fetchAuthor(1);

        assertThrows(AuthorNotFoundException.class, () -> bookService.addBook(bookDto));

        verify(authorService, times(1)).fetchAuthor(1);
        verify(bookRepository, times(0)).save(any(Book.class));
        verify(bookRepository, times(0)).existsByBookNameAndAuthor_AuthorId("GOF", 1);
    }

    @Test
    void ableToUpdateBookDetails() throws BookNotFoundException, AuthorNotFoundException {
        BookDto bookDtoInput = new BookDto();

        bookDtoInput.setBookName("GOF");
        bookDtoInput.setPublishedYear(1994);
        bookDtoInput.setAuthorId(1);
        bookDtoInput.setTotalQty(18);

        Book book = new Book();

        book.setBookName("GOF");
        book.setBookId(1);
        book.setPublishedYear(1994);
        book.setAvailableQty(10);
        book.setTotalQty(10);

        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorId(2);
        authorDto.setAuthorName("AB");

        when(bookRepository.findByBookName(bookDtoInput.getBookName())).thenReturn(book);
        when(authorService.fetchAuthor(bookDtoInput.getAuthorId())).thenReturn(Optional.of(authorDto));

        bookService.updateBook(bookDtoInput);

        verify(bookRepository, times(1)).findByBookName(bookDtoInput.getBookName());
        verify(authorService, times(1)).fetchAuthor(bookDtoInput.getAuthorId());
        verify(bookRepository, times(1)).save(any(Book.class));

    }

    @Test
    void updateBookShouldThrowBookNotFoundExceptionWhenBookDoesntExist() throws AuthorNotFoundException, BookNotFoundException {

        BookDto bookDtoInput = new BookDto();

        bookDtoInput.setBookName("GOF");
        bookDtoInput.setPublishedYear(1994);
        bookDtoInput.setAuthorId(1);
        bookDtoInput.setTotalQty(18);

        when(bookRepository.findByBookName("GOF"))
                .thenThrow(new BookNotFoundException("Book not found in the database"));

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(bookDtoInput);
        });

        assertEquals("Book not found in the database", exception.getMessage());

        verify(bookRepository, times(1)).findByBookName("GOF");
    }

    @Test
    void updateBookShouldThrowAuthorNotFoundExceptionWhenAuthorDoesntExist() throws AuthorNotFoundException {

        when(authorService.fetchAuthor(1))
                .thenThrow(new AuthorNotFoundException("Author details not found"));

        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () -> {
            authorService.fetchAuthor(1);
        });

        assertEquals("Author details not found", exception.getMessage());

        verify(authorService, times(1)).fetchAuthor(1);
    }

    @Test
    void shouldDeleteTheBookIfDetailsAreCorrect(){

        int authorId = 1;
        String bookName = "GOF";

        Author author = new Author();

        author.setAuthorId(1);
        author.setAuthorName("AkHi");

        Book bookFetched = new Book();
        bookFetched.setBookId(1);
        bookFetched.setBookName("GOF");
        bookFetched.setAuthor(author);
        bookFetched.setPublishedYear(1994);
        bookFetched.setTotalQty(10);
        bookFetched.setAvailableQty(8);
        bookFetched.setStatus("Available");

        when(authorService.checkAuthor(authorId)).thenReturn(true);

        when(bookRepository.existsByBookNameAndAuthor_AuthorId(bookName, authorId))
                .thenReturn(true);

        when(bookRepository.findByBookNameAndAuthor_AuthorId(bookName, authorId)).thenReturn(bookFetched);

        bookFetched.setStatus("Archieve");

        when(bookRepository.save(bookFetched)).thenReturn(bookFetched);

        BookDto bookDto  = bookService.deleteBook("GOF", 1);

        assertEquals(bookFetched.getBookName(), bookDto.getBookName());
        assertEquals(bookFetched.getBookId(), bookDto.getBookId());
        assertEquals("Archive", bookFetched.getStatus());
        assertEquals(bookFetched.getPublishedYear(), bookDto.getPublishedYear());
        assertEquals(bookFetched.getAuthor().getAuthorId(), bookDto.getAuthorId());
        assertEquals(bookFetched.getTotalQty(), bookDto.getTotalQty());
        assertEquals(bookFetched.getAvailableQty(), bookDto.getAvailableQty());

        verify(authorService, times(1)).checkAuthor(authorId);
        verify(bookRepository, times(1)).existsByBookNameAndAuthor_AuthorId(bookName, authorId);
        verify(bookRepository, times(1)).findByBookNameAndAuthor_AuthorId(bookName, authorId);
        verify(bookRepository, times(1)).save(bookFetched);
    }

}
