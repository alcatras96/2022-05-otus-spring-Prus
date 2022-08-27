package ru.otus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.service.api.BooksService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class, properties = "mongock.enabled=false")
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BooksService booksService;

    @Test
    void shouldAddCommentToBookCorrectly() throws Exception {
        var commentText = "text";
        mvc.perform(post("/api/v1/book/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentText)
                )
                .andExpect(status().isOk());

        verify(booksService).addComment("1", commentText);
    }

    @Test
    void shouldDeleteAllBookCommentsCorrectly() throws Exception {
        mvc.perform(delete("/api/v1/book/1/comment")).andExpect(status().isOk());

        verify(booksService).deleteCommentsById("1");
    }
}