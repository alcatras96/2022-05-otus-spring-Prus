package ru.otus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.dto.BookCreationDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.GenreDto;
import ru.otus.dto.converter.api.Converter;
import ru.otus.model.Author;
import ru.otus.model.Book;
import ru.otus.model.Comment;
import ru.otus.model.Genre;
import ru.otus.repository.api.AuthorRepository;
import ru.otus.repository.api.BookRepository;
import ru.otus.repository.api.GenreRepository;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RoutesConfiguration {

    @Bean
    public RouterFunction<ServerResponse> bookRoutes(BookRepository bookRepository,
                                                     AuthorRepository authorRepository,
                                                     Converter<Book, BookDto> bookDtoConverter,
                                                     Converter<BookCreationDto, Book> bookCreationDtoConverter) {
        return route()
                .GET("/api/v1/book",
                        request -> bookRepository.findAllWithRelations()
                                .map(bookDtoConverter::convert)
                                .collectList()
                                .flatMap(books -> ok().contentType(APPLICATION_JSON).bodyValue(books)))
                .GET("/api/v1/book/{id}",
                        request -> bookRepository.findByIdWithRelations(request.pathVariable("id"))
                                .map(bookDtoConverter::convert)
                                .flatMap(book -> ok().contentType(APPLICATION_JSON).bodyValue(book))
                                .switchIfEmpty(notFound().build())
                )
                .PUT("/api/v1/book/{id}/name",
                        request -> request.bodyToMono(String.class)
                                .flatMap(name -> bookRepository.findById(request.pathVariable("id"))
                                        .doOnNext(book -> book.setName(name)))
                                .flatMap(bookRepository::save)
                                .flatMap(unused -> ok().build())
                                .switchIfEmpty(notFound().build())
                )
                .POST("/api/v1/book",
                        request -> request.bodyToMono(BookCreationDto.class)
                                .map(bookCreationDtoConverter::convert)
                                .flatMap(book -> authorRepository.findByFullName(book.getAuthorName())
                                        .switchIfEmpty(authorRepository.save(book.getAuthor()))
                                        .map(Author::getId)
                                        .doOnNext(book::setAuthorId)
                                        .thenReturn(book)
                                )
                                .flatMap(bookRepository::save)
                                .then(ok().build())
                )
                .DELETE("/api/v1/book/{id}",
                        request -> bookRepository.deleteById(request.pathVariable("id"))
                                .then(ok().build())
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> commentRoutes(BookRepository bookRepository) {
        return route()
                .POST("/api/v1/book/{bookId}/comment",
                        request -> request.bodyToMono(String.class)
                                .flatMap(commentText -> bookRepository.findById(request.pathVariable("bookId"))
                                        .doOnNext(book -> book.addComment(new Comment(commentText))))
                                .flatMap(bookRepository::save)
                                .flatMap(unused -> ok().build())
                                .switchIfEmpty(notFound().build())
                )
                .DELETE("/api/v1/book/{bookId}/comment",
                        request -> bookRepository.findById(request.pathVariable("bookId"))
                                .doOnNext(book -> book.setComments(Collections.emptyList()))
                                .flatMap(bookRepository::save)
                                .flatMap(unused -> ok().build())
                                .switchIfEmpty(notFound().build())
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> genreRoutes(GenreRepository genreRepository,
                                                      Converter<Genre, GenreDto> genreDtoConverter) {
        return route()
                .GET("/api/v1/genre",
                        request -> genreRepository.findAll()
                                .map(genreDtoConverter::convert)
                                .collectList()
                                .flatMap(genres -> ok().contentType(APPLICATION_JSON).bodyValue(genres))
                )
                .build();
    }
}
