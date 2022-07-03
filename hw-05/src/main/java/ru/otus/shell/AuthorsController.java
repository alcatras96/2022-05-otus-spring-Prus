package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.Author;
import ru.otus.service.api.AuthorsService;
import ru.otus.shell.mapper.api.AuthorMapper;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorsController  {

    private final AuthorsService authorsService;
    private final AuthorMapper authorMapper;

    @ShellMethod(value = "Get all authors command", key = {"get_all_authors"})
    public List<Author> getAll() {
        return authorsService.getAll();
    }

    @ShellMethod(value = "Get author by id command", key = {"get_author_by_id"})
    public Author getById(@ShellOption Long id) {
        return authorsService.getById(id);
    }

    @ShellMethod(value = "Create author command", key = {"create_author"})
    public void create(@ShellOption String name) {
        Author author = authorMapper.map(name);
        authorsService.save(author);
    }

    @ShellMethod(value = "Update author name command", key = {"update_author_full_name"})
    public void update(@ShellOption Long id, @ShellOption String fullName) {
        authorsService.updateFullNameById(id, fullName);
    }

    @ShellMethod(value = "Delete author by id command", key = {"delete_author_by_id"})
    public void delete(@ShellOption Long id) {
        authorsService.deleteById(id);
    }
}
