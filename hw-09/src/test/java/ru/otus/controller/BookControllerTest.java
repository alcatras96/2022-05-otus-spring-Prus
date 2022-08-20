package ru.otus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.*;
import ru.otus.service.api.BooksService;
import ru.otus.service.api.GenresService;

import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class, properties = "mongock.enabled=false")
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BooksService booksService;

    @MockBean
    private GenresService genresService;

    @Test
    void shouldReturnAllBooksInModelForListPage() throws Exception {
        List<BookDto> expectedBooks = of(getFirstBookDto());

        given(booksService.getAll()).willReturn(expectedBooks);

        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", expectedBooks));
    }

    @Test
    void shouldReturnExpectedObjectsForEditBookFlowInModel() throws Exception {
        BookDto expectedBook = getFirstBookDto();

        given(booksService.getById("1")).willReturn(Optional.of(expectedBook));

        mvc.perform(get("/book/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("newComment", new CommentCreationDto()));
    }

    @Test
    void shouldReturnExpectedObjectsForNewBookFlowInModel() throws Exception {
        List<GenreDto> expectedGenres = of(new GenreDto("1", "comedy"), new GenreDto("2", "drama"));

        given(genresService.getAll()).willReturn(expectedGenres);

        mvc.perform(get("/book/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", new BookCreationDto()))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @Test
    void shouldReturnErrorIfNoBookFoundForEditPage() throws Exception {
        given(booksService.getById("1")).willReturn(Optional.empty());

        mvc.perform(get("/book/edit?id=1")).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBookCorrectly() throws Exception {

        mvc.perform(post("/book/new"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(booksService).save(any(BookCreationDto.class));
    }


    @Test
    void shouldUpdateBookNameCorrectly() throws Exception {

        mvc.perform(post("/book/name"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(booksService).updateName(any(BookUpdateDto.class));
    }

    @Test
    void shouldDeleteBookCorrectly() throws Exception {

        mvc.perform(post("/book/delete?id=1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(booksService).deleteById(any(String.class));
    }

    @Test
    void shouldAddCommentToBookCorrectly() throws Exception {

        mvc.perform(post("/book/comment"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(booksService).addComment(any(CommentCreationDto.class));
    }

    @Test
    void shouldDeleteAllBookCommentsCorrectly() throws Exception {

        mvc.perform(post("/book/comment/delete?id=1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        verify(booksService).deleteCommentsById(any(String.class));
    }

    private BookDto getFirstBookDto() {
        return BookDto.builder()
                .id("1")
                .name("book 1")
                .authorName("author 1")
                .genres(of("comedy", "drama"))
                .build();
    }
}