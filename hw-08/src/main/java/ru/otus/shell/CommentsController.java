package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.service.api.BooksService;
import ru.otus.shell.mapper.api.CommentMapper;
import ru.otus.view.converter.api.BookConverter;

@ShellComponent
@RequiredArgsConstructor
public class CommentsController {

    private final BooksService booksService;
    private final CommentMapper commentMapper;
    private final BookConverter bookViewConverter;

    @ShellMethod(value = "Get comments by book name command", key = {"get_comments_by_book_name"})
    public String getByBookId(@ShellOption String name) {
        return bookViewConverter.convertWithComments(booksService.getByName(name).orElseThrow());
    }

    @ShellMethod(value = "Create comment command", key = {"create_comment"})
    public void create(@ShellOption String value, String bookId) {
        booksService.addComment(bookId, commentMapper.map(value));
    }

    @ShellMethod(value = "Delete all comments by book id", key = {"delete_comments_by_book_id"})
    public void delete(@ShellOption String id) {
        booksService.deleteCommentsById(id);
    }
}
