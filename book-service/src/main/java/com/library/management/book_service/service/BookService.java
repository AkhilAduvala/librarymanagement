package com.library.management.book_service.service;

import com.library.management.book_service.dto.AuthorDto;
import com.library.management.book_service.dto.BookDto;
import com.library.management.book_service.model.Author;
import com.library.management.book_service.model.Book;
import com.library.management.book_service.repository.BookRepository;
import com.library.management.book_service.util.AuthorNotFoundException;
import com.library.management.book_service.util.BookAlreadyExists;
import com.library.management.book_service.util.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorService authorService){
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    public BookDto fetchBook(String bookName) throws BookNotFoundException {
        Book book = bookRepository.findByBookName(bookName);

        if(book == null) throw new BookNotFoundException("Book not found in the database");

        BookDto bookDto = new BookDto();

        bookDto.setBookId(book.getBookId());
        bookDto.setBookName(book.getBookName());
        bookDto.setPublishedYear(book.getPublishedYear());
        bookDto.setTotalQty(book.getTotalQty());
        bookDto.setAvailableQty(book.getAvailableQty());
        bookDto.setAuthorId(book.getAuthor().getAuthorId());
        bookDto.setStatus(book.getStatus());

        return bookDto;
    }

    public void addBook(BookDto bookDto) throws AuthorNotFoundException, BookAlreadyExists {

        AuthorDto authorDto = authorService.fetchAuthor(bookDto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + bookDto.getAuthorId()));

        boolean isBookExist = bookRepository.existsByBookNameAndAuthor_AuthorId(bookDto.getBookName(), bookDto.getAuthorId());

        if(isBookExist) throw  new BookAlreadyExists("Book : " + bookDto.getBookName() + " by : " +
                authorDto.getAuthorName() + " already exists");

        Author author = new Author();

        author.setAuthorId(authorDto.getAuthorId());
        author.setAuthorName(authorDto.getAuthorName());

        Book book = new Book();

        book.setBookName(bookDto.getBookName());
        book.setAuthor(author);
        book.setPublishedYear(bookDto.getPublishedYear());
        book.setTotalQty(bookDto.getTotalQty());
        book.setAvailableQty(bookDto.getAvailableQty());
        book.setStatus(String.valueOf(Book.Status.Available));

        Book addedBook = bookRepository.save(book);
    }

    public void updateBook(BookDto bookDtoInput) throws BookNotFoundException, AuthorNotFoundException{


            Book bookFetched = bookRepository.findByBookName(bookDtoInput.getBookName());

            if(bookFetched == null){
                throw new BookNotFoundException("Book not found in the database");
            }
            AuthorDto authorDto = authorService.fetchAuthor(bookDtoInput.getAuthorId())
                    .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: "
                            + bookDtoInput.getAuthorId()));

            Author author = new Author();

            author.setAuthorName(authorDto.getAuthorName());
            author.setAuthorId(authorDto.getAuthorId());

        Book book = getUpdateBookData(bookDtoInput, bookFetched, author);

        Book updatedBook = bookRepository.save(book);
    }

    private Book getUpdateBookData(BookDto bookDtoInput, Book book, Author author) {
        Book bookData = new Book();

        bookData.setBookId(book.getBookId());
        bookData.setBookName(book.getBookName());
        bookData.setAuthor(author);
        if(bookData.getPublishedYear() <= 0){
            bookData.setPublishedYear(book.getPublishedYear());
        }else{
            bookData.setPublishedYear(bookDtoInput.getPublishedYear());
        }
        if(bookDtoInput.getTotalQty() < 0){
            bookData.setTotalQty(book.getTotalQty());
            bookData.setAvailableQty(book.getAvailableQty());
        }else{
            bookData.setTotalQty(bookDtoInput.getTotalQty());
            bookData.setAvailableQty(book.getAvailableQty() + (bookDtoInput.getTotalQty() - book.getTotalQty()));
        }
        bookData.setStatus(String.valueOf(Book.Status.Available));
        return bookData;
    }

    public BookDto deleteBook(String bookName, int authorId){

        if(!authorService.checkAuthor(authorId))
            throw new AuthorNotFoundException("Author details not found");

        if(!bookRepository.existsByBookNameAndAuthor_AuthorId(bookName, authorId))
            throw new BookNotFoundException("Book not found");

        Book bookFetched = bookRepository.findByBookNameAndAuthor_AuthorId(bookName, authorId);

        bookFetched.setStatus(String.valueOf(Book.Status.Archive));

        Book bookDeleted = bookRepository.save(bookFetched);

        BookDto bookDto = new BookDto();

        bookDto.setBookName(bookDeleted.getBookName());
        bookDto.setBookId(bookDeleted.getBookId());
        bookDto.setPublishedYear(bookDeleted.getPublishedYear());
        bookDto.setTotalQty(bookDeleted.getTotalQty());
        bookDto.setAvailableQty(bookDeleted.getAvailableQty());
        bookDto.setStatus(bookDeleted.getStatus());
        bookDto.setAuthorId(bookDeleted.getAuthor().getAuthorId());

        return bookDto;
    }

}
