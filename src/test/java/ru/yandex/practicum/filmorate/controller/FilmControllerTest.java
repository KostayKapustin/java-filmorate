/*
package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    void clearingStorage() {
        inMemoryFilmStorage.getFilms().clear();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenThereAreNoFilmsInStorage() throws Exception {
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAll_shouldReturnCorrectListWithFilms_whenLeastOneFilmExistsInStorage() throws Exception {
        Film film = Film.builder()
                .name("Май")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .name("Май2")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON));
        String json2 = objectMapper.writeValueAsString(film2);
        this.mockMvc.perform(post("/films").content(json2).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("Май2")));
    }

    @Test
    void create_throwsInvalidNameException_whenEmptyFilmNamePassed() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        MvcResult response = this.mockMvc.perform(post("/films")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Название фильма не может быть пустым."));
    }

    @Test
    void create_throwsInvalidNameException_whenEmptyNamePassed() throws Exception {
        Film film = Film.builder()
                .name(null)
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        MvcResult response = this.mockMvc.perform(post("/films")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Имя не может равняться null"));
    }

    @Test
    void create_throwsInvalidDescriptionException_descriptionMoreThan200CharactersPassed() throws Exception {
        Film film = Film.builder()
                .name("Последний богатырь")
                .description("Главный герой, Иван Найдёнов (Виктор Хориняк) живёт в Москве." +
                        " С материальной точки зрения Иван живёт неплохо —" +
                        " под именем «белый маг Светозар» он участвует в телешоу «Битва магов», " +
                        "ведёт частный приём состоятельных клиенток, имеет квартиру в Москва-Сити, " +
                        "но он очень одинок.")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        MvcResult response = this.mockMvc.perform(post("/films")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Описание может сосотоять из максимум 200 симвалов."));
    }

    @Test
    void create_throwsInvalidExceptionDurationFilm_whenNegativeNumberPassed() throws Exception {
        Film film = Film.builder()
                .name("Не пустой")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(-100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        MvcResult response = this.mockMvc.perform(post("/films")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Продолжительность фильма должна быть положительной и не равным текущей дате."));
    }

    @Test
    void create_shouldReturnCorrectFilm_whenCorrectDataPasse() throws Exception {
        Film film = Film.builder()
                .name("Не пустой")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("200")))
                .andExpect(jsonPath("$.releaseDate", is("" +
                        LocalDate.of(2012, 12, 12))))
                .andExpect(jsonPath("$.duration", is(100)))
                .andExpect(jsonPath("$.name", is("Не пустой")));
    }

    @Test
    void update_shouldReturnFilmWithTheCorrectData_whenCorrectDataPassed() throws Exception {
        Film film = Film.builder()
                .name("Не пустой")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .id(4)
                .name("Не пустой 2")
                .description("200-200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON));
        String json2 = objectMapper.writeValueAsString(film2);
        this.mockMvc.perform(put("/films").content(json2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("200-200")))
                .andExpect(jsonPath("$.name", is("Не пустой 2")));
    }

    @Test
    void create_throwsInvalidDateException_whenInvalidDatePassed() {
        Film film = Film.builder()
                .name("Не пустое")
                .description("<200")
                .releaseDate(LocalDate.of(1, 12, 12))
                .duration(100)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> inMemoryFilmStorage.create(film));
    }

    @Test
    void update_throwsException_whenFilmWithPassedIdNotFound() {
        Film film = Film.builder()
                .id(1)
                .name("Не пустой")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        Assertions.assertThrows(ResponseStatusException.class, () -> inMemoryFilmStorage.update(film));
    }
}
*/
