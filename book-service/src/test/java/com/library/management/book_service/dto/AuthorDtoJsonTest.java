package com.library.management.book_service.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class AuthorDtoJsonTest {

    @Autowired
    private JacksonTester<AuthorDto> json;

    @Test
    void authorSerializationTest() throws IOException{

        AuthorDto authorDto = new AuthorDto();

        authorDto.setAuthorId(1);
        authorDto.setAuthorName("GOF4");

        String expectedJson = """
        {
            "authorId": 1,
            "authorName": "GOF4"
        }
        """;

        assertThat(json.write(authorDto)).isStrictlyEqualToJson(expectedJson);

        assertThat(json.write(authorDto)).hasJsonPathNumberValue("@.authorId");
        assertThat(json.write(authorDto)).extractingJsonPathNumberValue("@.authorId")
                .isEqualTo(1);

        assertThat(json.write(authorDto)).hasJsonPathStringValue("@.authorName");
        assertThat(json.write(authorDto)).extractingJsonPathStringValue("@.authorName")
                .isEqualTo("GOF4");
    }

    @Test
    void authorDeserializationTest() throws IOException{

        String jsonContent = """
        {
            "authorId": 1,
            "authorName": "GOF4"
        }
        """;

        AuthorDto authorDto = json.parseObject(jsonContent);

        assertThat(authorDto.getAuthorId()).isEqualTo(1);
        assertThat(authorDto.getAuthorName()).isEqualTo("GOF4");
    }
}