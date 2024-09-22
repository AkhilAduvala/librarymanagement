package com.library.management.book_service.service;

import com.library.management.book_service.dto.AuthorDto;
import com.library.management.book_service.model.Author;
import com.library.management.book_service.repository.AuthorRepository;
import com.library.management.book_service.util.AuthorNotFoundException;
import com.library.management.book_service.util.DuplicateAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public Optional<AuthorDto> fetchAuthor(int authorId) throws AuthorNotFoundException {

        Author author = authorRepository.findByAuthorId(authorId);

        if (author == null) {
            return Optional.empty();
        }

        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorId(author.getAuthorId());
        authorDto.setAuthorName(author.getAuthorName());

        return Optional.of(authorDto);
    }

    public void addAuthor(AuthorDto authorDto) throws DuplicateAuthorException {

        if(authorRepository.existsByAuthorName(authorDto.getAuthorName())){
            throw new DuplicateAuthorException("Author already exist");
        }

        Author author = new Author();

        author.setAuthorName(authorDto.getAuthorName());
        authorRepository.save(author);
    }

    public boolean checkAuthor(int authorId) throws AuthorNotFoundException {
        if (authorRepository.existsByAuthorId(authorId)){
            return true;
        }else{
            throw new AuthorNotFoundException("Author details not found");
        }
    }
}
