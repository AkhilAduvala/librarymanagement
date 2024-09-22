package com.library.management.book_service.repository;

import com.library.management.book_service.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

    Book findByBookName(String bookName);

    Book findByBookNameAndAuthor_AuthorId(String bookName, int authorId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM books WHERE book_name = :bookName AND " +
            "author_id = :authorId AND status = 'Available')", nativeQuery = true)
    boolean existsByBookNameAndAuthor_AuthorId(@Param("bookName") String bookName, @Param("authorId") int authorId);

}
