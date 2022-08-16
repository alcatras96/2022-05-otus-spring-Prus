package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.model.Book;
import ru.otus.service.api.BooksService;
import ru.otus.shell.mapper.api.BookMapper;
import ru.otus.view.converter.api.ViewConverter;

@ShellComponent
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;
    private final BookMapper bookMapper;
    private final ViewConverter<Book> bookViewConverter;

    @ShellMethod(value = "Get all books command", key = {"get_all_books"})
    public String getAll() {
        return bookViewConverter.convert(booksService.getAll(), System.lineSeparator());
    }

    @ShellMethod(value = "Get book by id command", key = {"get_book_by_id"})
    public String getById(@ShellOption String id) {
        return booksService.getById(id)
                .map(bookViewConverter::convert)
                .orElse("No book found.");
    }

    @ShellMethod(value = "Create book command", key = {"create_book"})
    public void create(@ShellOption String name, @ShellOption String authorId, @ShellOption String genresIds) {
        var book = bookMapper.map(name, authorId, genresIds);
        booksService.save(book);
    }

    @ShellMethod(value = "Update book name command", key = {"update_book_name"})
    public void update(@ShellOption String id, @ShellOption String name) {
        booksService.updateNameById(id, name);
    }

    @ShellMethod(value = "Delete book by id command", key = {"delete_book_by_id"})
    public void delete(@ShellOption String id) {
        booksService.deleteById(id);
    }
}
