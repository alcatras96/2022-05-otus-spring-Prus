package ru.otus.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.GenreDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookRoutesTest {

    @Autowired
    private RouterFunction<ServerResponse> bookRoutes;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private Converter<Book, BookDto> bookDtoConverter;

    @MockBean
    private Converter<BookCreationDto, Book> bookCreationDtoConverter;

    @Test
    public void shouldReturnAllBooks() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        BookDto expectedBook = getFirstBookDto();
        when(bookRepository.findAllWithRelations()).thenReturn(Flux.just(new Book()));
        when(bookDtoConverter.convert(any(Book.class))).thenReturn(expectedBook);

        client.get()
                .uri("/api/v1/book")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDto.class)
                .isEqualTo(of(expectedBook));
    }

    @Test
    public void shouldReturnBookById() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        BookDto expectedBook = getFirstBookDto();
        when(bookRepository.findByIdWithRelations(any(String.class))).thenReturn(Mono.just(new Book()));
        when(bookDtoConverter.convert(any(Book.class))).thenReturn(expectedBook);

        client.get()
                .uri("/api/v1/book/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(expectedBook);
    }

    @Test
    public void shouldDeleteBookById() {
        var client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        when(bookRepository.deleteById(any(String.class))).thenReturn(Mono.empty());

        var bookId = "1";

        client.delete()
                .uri("/api/v1/book/" + bookId)
                .exchange()
                .expectStatus()
                .isOk();

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    public void shouldChangeBookName() {
        var client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(Mono.just(new Book()));
        when(bookRepository.findById(any(String.class))).thenReturn(Mono.just(new Book()));

        var bookId = "1";
        var newName = "new name";
        client.put()
                .uri("/api/v1/book/" + bookId + "/name")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newName))
                .exchange()
                .expectStatus()
                .isOk();

        var expectedBook = new Book();
        expectedBook.setName(newName);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(expectedBook);
    }

    @Test
    public void shouldAddBook() {
        var client = WebTestClient
                .bindToRouterFunction(bookRoutes)
                .build();

        var bookCreationDto = new BookCreationDto("book name", "author name", of("1"));
        var author = new Author("1", bookCreationDto.getAuthorName());
        var expectedBook = Book.builder()
                .name(bookCreationDto.getName())
                .authorId(author.getId())
                .author(author)
                .genreIds(bookCreationDto.getGenreIds())
                .build();

        when(bookCreationDtoConverter.convert(any(BookCreationDto.class))).thenReturn(expectedBook);
        when(bookRepository.save(any(Book.class))).thenReturn(Mono.empty());
        when(authorRepository.findByFullName(any(String.class))).thenReturn(Mono.just(author));
        when(authorRepository.save(any(Author.class))).thenReturn(Mono.just(author));

        client.post()
                .uri("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookCreationDto))
                .exchange()
                .expectStatus()
                .isOk();


        verify(bookRepository).save(expectedBook);
    }

    private BookDto getFirstBookDto() {
        return BookDto.builder()
                .id("1")
                .name("book 1")
                .authorName("author 1")
                .genres(of(new GenreDto("comedy"), new GenreDto("drama")))
                .build();
    }
}