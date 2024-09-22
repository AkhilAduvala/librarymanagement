package com.library.management.book_service.controller;


import com.library.management.book_service.dto.BookDto;
import com.library.management.book_service.model.Book;
import com.library.management.book_service.util.AuthorNotFoundException;
import com.library.management.book_service.util.BookAlreadyExists;
import com.library.management.book_service.util.BookNotFoundException;
import com.library.management.book_service.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnABookDtoWhenABookExists() throws Exception {

        String bookName = "GOF";

        BookDto bookDto = new BookDto();
        bookDto.setBookId(1);
        bookDto.setBookName("GOF");
        bookDto.setAuthorId(1);
        bookDto.setPublishedYear(1994);
        bookDto.setTotalQty(10);
        bookDto.setAvailableQty(4);

        when(bookService.fetchBook(bookName)).thenReturn(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/book/fetchBook")
                        .param("bookName", bookName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(bookDto)));
    }

    @Test
    void shouldReturnBookNotFoundWhenBookDoesNotExist() throws Exception {
        String bookName = "UnknownBook";

        when(bookService.fetchBook(bookName)).thenThrow(new BookNotFoundException("Book not found in the database"));

        mockMvc.perform(MockMvcRequestBuilders.get("/book/fetchBook")
                        .param("bookName", bookName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldBeAbleTOAddBookWhenBookDoesNotExists() throws Exception {

        BookDto bookDto = new BookDto();

        bookDto.setBookId(1);
        bookDto.setBookName("GOF");
        bookDto.setAuthorId(1);
        bookDto.setPublishedYear(1994);
        bookDto.setTotalQty(10);
        bookDto.setAvailableQty(8);

        doNothing().when(bookService).addBook(any(BookDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/book/addBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(bookService, times(1)).addBook(bookDto);
    }

    @Test
    void shouldReturnAAuthorNotFoundExceptionWhenAuthorDoesNotExist() throws Exception {
        BookDto bookDto = new BookDto();

        bookDto.setBookId(1);
        bookDto.setBookName("GOF");
        bookDto.setAuthorId(1);
        bookDto.setPublishedYear(1994);
        bookDto.setTotalQty(10);
        bookDto.setAvailableQty(8);

        doThrow(new AuthorNotFoundException("Author not found with ID: 1"))
                .when(bookService).addBook(any(BookDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/book/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).addBook(bookDto);
    }

    @Test
    void shouldReturnABookAlreadyExistExceptionWhenBookExist() throws Exception {

        BookDto bookDto = new BookDto();

        bookDto.setBookId(1);
        bookDto.setBookName("GOF");
        bookDto.setAuthorId(1);
        bookDto.setPublishedYear(1994);
        bookDto.setTotalQty(10);
        bookDto.setAvailableQty(8);

        doThrow(new BookAlreadyExists("Book details already exists in the database"))
                .when(bookService).addBook(any(BookDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/book/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void shouldBeAbleToUpdateBookDetails() throws Exception {

        BookDto bookDtoInput = new BookDto();

        bookDtoInput.setBookName("GOF");
        bookDtoInput.setPublishedYear(1994);
        bookDtoInput.setAuthorId(1);
        bookDtoInput.setTotalQty(18);

        doNothing().when(bookService).updateBook(bookDtoInput);

        mockMvc.perform(MockMvcRequestBuilders.put("/book/updateBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDtoInput))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(bookService, times(1)).updateBook(bookDtoInput);
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenUpdatingBookThatDoesNotExist() throws Exception {

        BookDto bookDtoInput = new BookDto();

        bookDtoInput.setBookName("GOF");
        bookDtoInput.setPublishedYear(1994);
        bookDtoInput.setAuthorId(1);
        bookDtoInput.setTotalQty(18);

        doThrow(new BookNotFoundException("Book not found in the database"))
                .when(bookService).updateBook(bookDtoInput);

        mockMvc.perform(MockMvcRequestBuilders.put("/book/updateBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDtoInput))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).updateBook(bookDtoInput);
    }

    @Test
    void shouldThrowAuthorNotFoundExceptionWhenUpdatingBookThatDoesNotExist() throws Exception {

        BookDto bookDtoInput = new BookDto();

        bookDtoInput.setBookName("GOF");
        bookDtoInput.setPublishedYear(1994);
        bookDtoInput.setAuthorId(1);
        bookDtoInput.setTotalQty(18);

        doThrow(new AuthorNotFoundException("Author not found with ID: 1"))
                .when(bookService).updateBook(bookDtoInput);

        mockMvc.perform(MockMvcRequestBuilders.put("/book/updateBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDtoInput))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(bookService, times(1)).updateBook(bookDtoInput);
    }

    @Test
    void shouldeBeAbleToDeleteABook() throws Exception {
        String bookName = "GOF4";
        int authorId = 1;

        BookDto bookDto = new BookDto();

        when(bookService.deleteBook(bookName, authorId)).thenReturn(bookDto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/book/deleteBook")
                .contentType(MediaType.APPLICATION_JSON)
                .param("bookName", bookName)
                        .param("authorId", String.valueOf(authorId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
