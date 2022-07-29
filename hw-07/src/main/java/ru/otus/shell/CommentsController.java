package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.model.Comment;
import ru.otus.service.api.CommentsService;
import ru.otus.shell.mapper.api.CommentMapper;
import ru.otus.view.converter.api.ViewConverter;

@ShellComponent
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;
    private final CommentMapper commentMapper;
    private final ViewConverter<Comment> commentViewConverter;

    @ShellMethod(value = "Get all comments command", key = {"get_all_comments"})
    public String getAll() {
        return commentViewConverter.convert(commentsService.getAll(), System.lineSeparator());
    }

    @ShellMethod(value = "Get comment by id command", key = {"get_comment_by_id"})
    public String getById(@ShellOption Long id) {
        return commentsService.getById(id)
                .map(commentViewConverter::convert)
                .orElse("No comment found.");
    }

    @ShellMethod(value = "Get comments by book id command", key = {"get_comment_by_book_id"})
    public String getByBookId(@ShellOption Long id) {
        return commentViewConverter.convert(commentsService.getCommentsByBookId(id), System.lineSeparator());
    }

    @ShellMethod(value = "Create comment command", key = {"create_comment"})
    public void create(@ShellOption String value, Long bookId) {
        commentsService.save(commentMapper.map(value, bookId));
    }

    @ShellMethod(value = "Update comment value command", key = {"update_comment_value"})
    public void update(@ShellOption Long id, @ShellOption String value) {
        commentsService.updateTextById(id, value);
    }

    @ShellMethod(value = "Delete comment by id command", key = {"delete_comment_by_id"})
    public void delete(@ShellOption Long id) {
        commentsService.deleteById(id);
    }
}
