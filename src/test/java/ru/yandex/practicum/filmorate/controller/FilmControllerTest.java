package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.hamcrest.Matchers.*;
import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = FilmController.class)

public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    FilmController controller;

    @BeforeEach
    void clearingStorage() {
        controller.getFilms().clear();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenThereAreNoFilmsInStorage() throws Exception {
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAll_shouldReturnCorrectListWithFilms_whenAtLeastOneFilmExistsInStorage() throws Exception {
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
    void create_throwsInvalidNameException_whenEmptyMovieNamePassed() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_throwsInvalidDescription_exceptionDescriptionOfMoreThan200CharactersPassed() throws Exception {
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
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_outputsInvalidMovieDurationValue_whenNegative () throws Exception {
        Film film = Film.builder()
                .name("Не пустой")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(-100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void create_shouldReturnFilmWithTheCorrectData_whenTransmittingTheCorrectData() throws Exception{
        Film film = Film.builder()
                .name("Не пустой")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        String json = objectMapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(json).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("200")))
                .andExpect(jsonPath("$[0].releaseDate", is("" +
                        LocalDate.of(2012, 12, 12))))
                .andExpect(jsonPath("$[0].duration", is(100)))
                .andExpect(jsonPath("$[0].name", is("Не пустой")));
    }

    @Test
    void put_shouldReturnFilmWithTheCorrectData_whenTransmittingTheCorrectData() throws Exception {
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
        this.mockMvc.perform(put("/films").content(json2).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(get("/films").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is("200-200")))
                .andExpect(jsonPath("$[0].name", is("Не пустой 2")));
    }

    @Test
    void create_throwsInvalidDateException_whenInvalidDatePassed() {
        Film film = Film.builder()
                .name("Не пустое")
                .description("<200")
                .releaseDate(LocalDate.of(1, 12, 12))
                .duration(100)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.create(film));
    }

    @Test
    void put_throwsAnInvalidExceptionForChangingFilm_ExceptionIfYouChangeANonExistentFilm(){
        Film film = Film.builder()
                .id(1)
                .name("Не пустой")
                .description("200")
                .releaseDate(LocalDate.of(2012, 12, 12))
                .duration(100)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> controller.put(film));
    }
}
