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
    public String getById(@ShellOption Long id) {
        return bookViewConverter.convert(booksService.getById(id));
    }

    @ShellMethod(value = "Create book command", key = {"create_book"})
    public void create(@ShellOption String name, @ShellOption Long authorId, @ShellOption String genresIds) {
        Book book = bookMapper.map(name, authorId, genresIds);
        booksService.save(book);
    }

    @ShellMethod(value = "Update book name command", key = {"update_book_name"})
    public void update(@ShellOption Long id, @ShellOption String name) {
        booksService.updateName(id, name);
    }

    @ShellMethod(value = "Delete book by id command", key = {"delete_book_by_id"})
    public void delete(@ShellOption Long id) {
        booksService.deleteById(id);
    }
}
