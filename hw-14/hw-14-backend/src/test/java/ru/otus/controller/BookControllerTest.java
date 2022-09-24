package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.GenreDto;
import ru.otus.service.api.BooksService;

import java.util.Optional;

import static java.util.List.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class, properties = "mongock.enabled=false")
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BooksService booksService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnAllBooks() throws Exception {
        var expectedBooks = of(getFirstBookDto());

        given(booksService.getAll()).willReturn(expectedBooks);

        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @Test
    void shouldReturnErrorIfNoBookFound() throws Exception {
        given(booksService.getById("1")).willReturn(Optional.empty());

        mvc.perform(get("/api/v1/book/1")).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBookCorrectly() throws Exception {
        var bookCreationDto = new BookCreationDto("book name", "author name", of("1"));
        mvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookCreationDto))
                )
                .andExpect(status().isOk());

        verify(booksService).save(bookCreationDto);
    }


    @Test
    void shouldUpdateBookNameCorrectly() throws Exception {
        var bookId = "1";
        var bookName = "new name";

        mvc.perform(put("/api/v1/book/{id}/name", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookName)
                )
                .andExpect(status().isOk());

        verify(booksService).updateName(bookId, bookName);
    }

    @Test
    void shouldDeleteBookCorrectly() throws Exception {
        mvc.perform(delete("/api/v1/book/1")).andExpect(status().isOk());

        verify(booksService).deleteById("1");
    }

    private BookDto getFirstBookDto() {
        return BookDto.builder()
                .id("1")
                .name("book 1")
                .authorName("author 1")
                .genres(of(new GenreDto("comedy"), new GenreDto("drama")))
                .build();
    }
}