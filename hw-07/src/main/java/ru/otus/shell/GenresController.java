package ru.otus.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.model.Genre;
import ru.otus.service.api.GenresService;
import ru.otus.shell.mapper.api.GenreMapper;
import ru.otus.view.converter.api.ViewConverter;

@ShellComponent
@RequiredArgsConstructor
public class GenresController {

    private final GenresService genresService;
    private final GenreMapper genreMapper;
    private final ViewConverter<Genre> genreViewConverter;

    @ShellMethod(value = "Get all genres command", key = {"get_all_genres"})
    public String getAll() {
        return genreViewConverter.convert(genresService.getAll(), System.lineSeparator());
    }

    @ShellMethod(value = "Get genre by id command", key = {"get_genre_by_id"})
    public String getById(@ShellOption Long id) {
        return genresService.getById(id)
                .map(genreViewConverter::convert)
                .orElse("No genre found.");    }

    @ShellMethod(value = "Create genre command", key = {"create_genre"})
    public void create(@ShellOption String name) {
        var genre = genreMapper.map(name);
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
