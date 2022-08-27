package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.exception.NotFoundException;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDto;
import ru.otus.service.api.BooksService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {

    private final BooksService booksService;

    @GetMapping("/book")
    public List<BookDto> getAll() {
        return booksService.getAll();
    }

    @GetMapping("/book/{id}")
    public BookDto getById(@PathVariable String id) {
        return booksService.getById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping("/book")
    public void create(@RequestBody BookCreationDto bookCreationDto) {
        booksService.save(bookCreationDto);
    }

    @DeleteMapping("/book/{id}")
    public void delete(@PathVariable String id) {
        booksService.deleteById(id);
    }

    @PutMapping("/book/{id}/name")
    public void updateName(@PathVariable String id, @RequestBody String name) {
        booksService.updateName(id, name);
    }
}
