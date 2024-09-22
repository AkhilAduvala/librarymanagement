package com.library.management.book_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.book_service.dto.AuthorDto;
import com.library.management.book_service.service.AuthorService;
import com.library.management.book_service.util.DuplicateAuthorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp(){
        openMocks(this);
    }

    @Test
    void shouldReturnAAuthorDtoWhenAuthorExists() throws Exception {
        int authorId = 1;

        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorId(1);
        authorDto.setAuthorName("Akhil");

        when(authorService.fetchAuthor(authorId)).thenReturn(Optional.of(authorDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/author/fetchAuthor")
                .param("authorId", String.valueOf(authorId))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(authorDto)));
    }

    @Test
    void shouldReturnANotFoundExceptionWhenAuthorDoesNotExists() throws Exception {
        int authorId = 2;

        when(authorService.fetchAuthor(authorId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/author/fetchAuthor")
                .param("authorId", String.valueOf(authorId))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldBeAbleToAddAAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorName("Alex");

        doNothing().when(authorService).addAuthor(any(AuthorDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/author/addAuthor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(authorService, times(1)).addAuthor(authorDto);
    }

    @Test
    void shouldReturnDuplicateAuthorExceptionWhenAuthorExists() throws Exception {

        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorName("Akhil");

        doThrow(new DuplicateAuthorException("Author already exists"))
                .when(authorService).addAuthor(any(AuthorDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/author/addAuthor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
