package com.library.management.book_service.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookDtoJsonTest {

    @Autowired
    private JacksonTester<BookDto> json;

    @Test
    void bookSerializationTest() throws IOException {

        // Creating the BookDto objects
        BookDto bookDto = new BookDto();

        bookDto.setBookId(1);
        bookDto.setBookName("GOF");
        bookDto.setAuthorId(1);
        bookDto.setPublishedYear(1994);
        bookDto.setTotalQty(10);
        bookDto.setAvailableQty(4);

        // Compare the generated JSON with the expected inline JSON
        String expectedJson = """
        {
            "bookId": 1,
            "bookName": "GOF",
            "authorId": 1,
            "publishedYear": 1994,
            "totalQty": 10,
            "availableQty": 4
        }
        """;

        //Checks that the generated JSON matches the expected JSON exactly
        assertThat(json.write(bookDto)).isStrictlyEqualToJson(expectedJson);

        // Validate specific JSON paths
        assertThat(json.write(bookDto)).hasJsonPathStringValue("@.bookName");
        assertThat(json.write(bookDto)).extractingJsonPathStringValue("@.bookName").isEqualTo("GOF");

        assertThat(json.write(bookDto)).hasJsonPathNumberValue("@.bookId");
        assertThat(json.write(bookDto)).extractingJsonPathNumberValue("@.bookId").isEqualTo(1);

        assertThat(json.write(bookDto)).hasJsonPathNumberValue("@.totalQty");
        assertThat(json.write(bookDto)).extractingJsonPathNumberValue("@.totalQty").isEqualTo(10);

        assertThat(json.write(bookDto)).hasJsonPathNumberValue("@.availableQty");
        assertThat(json.write(bookDto)).extractingJsonPathNumberValue("@.availableQty").isEqualTo(4);

        assertThat(json.write(bookDto)).hasJsonPathNumberValue("@.publishedYear");
        assertThat(json.write(bookDto)).extractingJsonPathNumberValue("@.publishedYear").isEqualTo(1994);

        assertThat(json.write(bookDto)).hasJsonPathNumberValue("@.authorId");
        assertThat(json.write(bookDto)).extractingJsonPathNumberValue("@.authorId").isEqualTo(1);
    }

    @Test
    void bookDeserializationTest() throws IOException {
        // Deserialization test
        String jsonContent = """
        {
            "bookId": 1,
            "bookName": "GOF",
            "authorId": 1,
            "publishedYear": 1994,
            "totalQty": 10,
            "availableQty": 4
        }
        """;

        BookDto bookDto = json.parseObject(jsonContent);

        // Assertions
        assertThat(bookDto.getBookId()).isEqualTo(1);
        assertThat(bookDto.getBookName()).isEqualTo("GOF");
        assertThat(bookDto.getAuthorId()).isEqualTo(1);
        assertThat(bookDto.getPublishedYear()).isEqualTo(1994);
        assertThat(bookDto.getTotalQty()).isEqualTo(10);
        assertThat(bookDto.getAvailableQty()).isEqualTo(4);
    }
}
