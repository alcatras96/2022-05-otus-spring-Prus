package ru.otus.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.CommentCreationDto;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.repository.api.BookRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class BooksServiceTest {

    @Autowired
    private BooksServiceMongo booksServiceMongo;

    @MockBean
    private BookRepository bookRepository;

    @DisplayName("Should delete comments by book id.")
    @Test
    void shouldDeleteCommentsByBookId() {
        String bookId = "1";
        Book book = spy(Book.builder().id(bookId).build());
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));

        booksServiceMongo.deleteCommentsById(bookId);

        verify(bookRepository).findById(bookId);
        verify(book).setComments(Collections.emptyList());
        verify(bookRepository).save(book);
    }

    @DisplayName("Should create comment successfully.")
    @Test
    void shouldCreateCommentSuccessfully() {
        String bookId = "1";
        Book book = spy(Book.builder().id(bookId).comments(new ArrayList<>()).build());
        Comment comment = new Comment("new comment");
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book));

        booksServiceMongo.addComment(new CommentCreationDto(bookId, comment.getText()));

        verify(bookRepository).findById(bookId);
        verify(book).addComment(comment);
        verify(bookRepository).save(book);
    }
}