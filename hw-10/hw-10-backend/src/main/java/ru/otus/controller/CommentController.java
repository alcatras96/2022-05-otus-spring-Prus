package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.service.api.BooksService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

    private final BooksService booksService;

    @PostMapping("/book/{bookId}/comment")
    public void createComment(@PathVariable String bookId, @RequestBody String text) {
        booksService.addComment(bookId, text);
    }

    @DeleteMapping("/book/{bookId}/comment")
    public void deleteCommentsById(@PathVariable String bookId) {
        booksService.deleteCommentsById(bookId);
    }
}
