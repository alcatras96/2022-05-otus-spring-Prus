package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.dto.GenreDto;
import ru.otus.service.api.GenresService;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class, properties = "mongock.enabled=false")
class GenreControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenresService genresService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnAllGenres() throws Exception {
        var expectedGenres = of(new GenreDto("comedy"), new GenreDto("drama"));

        given(genresService.getAll()).willReturn(of(new GenreDto("comedy"), new GenreDto("drama")));

        mvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedGenres)));
    }

}