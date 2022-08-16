package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.controller.exception.NotFoundException;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDeletionDto;
import ru.otus.dto.BookUpdateDto;
import ru.otus.dto.CommentCreationDto;
import ru.otus.service.api.BooksService;
import ru.otus.service.api.GenresService;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BooksService booksService;
    private final GenresService genresService;

    @GetMapping
    public String listPage(Model model) {
        model.addAttribute("books", booksService.getAll());
        model.addAttribute("editableBookId", new BookDeletionDto());
        return "list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id == null) {
            model.addAttribute("genres", genresService.getAll());
            model.addAttribute("book", new BookCreationDto());
        } else {
            model.addAttribute("newComment", new CommentCreationDto());
            model.addAttribute("book", booksService.getById(id).orElseThrow(NotFoundException::new));
        }
        return "edit";
    }

    @PostMapping("/new")
    public String create(BookCreationDto bookCreationDto) {
        booksService.save(bookCreationDto);
        return "redirect:/";
    }

    @PostMapping("/name")
    public String updateName(BookUpdateDto bookUpdateDto) {
        booksService.updateName(bookUpdateDto);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(BookDeletionDto bookDeletionDto) {
        booksService.deleteById(bookDeletionDto.getId());
        return "redirect:/";
    }

    @PostMapping("/comment")
    public String createComment(CommentCreationDto commentCreationDto) {
        booksService.addComment(commentCreationDto);
        return "redirect:/";
    }

    @PostMapping("/comment/delete")
    public String deleteCommentsById(BookDeletionDto bookDeletionDto) {
        booksService.deleteCommentsById(bookDeletionDto.getId());
        return "redirect:/";
    }
}
