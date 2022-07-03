package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.domain.Genre;
import ru.otus.service.api.GenresService;
import ru.otus.shell.mapper.api.GenreMapper;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenresController {

    private final GenresService genresService;
    private final GenreMapper genreMapper;

    @ShellMethod(value = "Get all genres command", key = {"get_all_genres"})
    public List<Genre> getAll() {
        return genresService.getAll();
    }

    @ShellMethod(value = "Get genre by id command", key = {"get_genre_by_id"})
    public Genre getById(@ShellOption Long id) {
        return genresService.getById(id);
    }

    @ShellMethod(value = "Create genre command", key = {"create_genre"})
    public void create(@ShellOption String name) {
        Genre genre = genreMapper.map(name);
        genresService.save(genre);
    }

    @ShellMethod(value = "Update genre name command", key = {"update_genre_name"})
    public void update(@ShellOption Long id, @ShellOption String name) {
        genresService.updateNameById(id, name);
    }

    @ShellMethod(value = "Delete genre by id command", key = {"delete_genre_by_id"})
    public void delete(@ShellOption Long id) {
        genresService.deleteById(id);
    }
}
