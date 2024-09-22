package com.library.management.book_service.repository;

import com.library.management.book_service.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author findByAuthorName(String authorName);

    Author findByAuthorId(int authorId);

    boolean existsByAuthorName(String authorName);

    boolean existsByAuthorId(int authorId);
}
