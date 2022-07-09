package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.Book;
import ru.otus.domain.Genre;
import ru.otus.service.api.BooksService;
import ru.otus.shell.mapper.api.BookMapper;

import java.util.Collections;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;
    private final BookMapper bookMapper;

    @ShellMethod(value = "Get all books command", key = {"get_all_books"})
    public List<Book> getAll() {
        return booksService.getAll();
    }

    @ShellMethod(value = "Get book by id command", key = {"get_book_by_id"})
    public Book getById(@ShellOption Long id) {
        return booksService.getById(id);
    }

    @ShellMethod(value = "Test get all books performance command", key = {"test_get_all_books_performance"})
    public void testGetAllBooksPerformance() {
        booksService.getAll();

        booksService.getAllInOneQuery();

    }

    @ShellMethod(value = "Create N books command", key = {"create_n_dummy_books"})
    public void createDummyBooks(@ShellOption Integer number) {
        Book book = new Book("dummy", 1L);
        book.setGenres(List.of(new Genre(1L), new Genre(2L), new Genre(3L), new Genre(4L)));
        booksService.saveAll(Collections.nCopies(number, book));
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
