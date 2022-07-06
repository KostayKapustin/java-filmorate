package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    @Test
    @DisplayName("Запрос фильмов с пустым списком.")
    void findAllNull(){
        Assertions.assertEquals(0, filmController.findAll().size());
    }

    @Test
    @DisplayName("Запрос фильмов.")
    void findAll(){
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(2000, 10, 10),
                2000);
        filmController.addFilm(film);
        Assertions.assertEquals(1, filmController.findAll().size());
    }

    @Test
    @DisplayName("Создание film с пустым именем")
    void addFilmWithBlankName() throws RuntimeException {
        Film film = new Film(
                1,
                "",
                "description",
                LocalDate.of(2000, 10, 10),
                100);
        Assertions.assertThrows(InvalidEmailException.class, () -> filmController.addFilm(film));
    }

    @Test
    @DisplayName("Mаксимальная длина описания — 200 символов.Правельный вариант.")
    void addFilmLessThan200Characters() throws RuntimeException {
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(1990, 10, 10),
                50);
        filmController.addFilm(film);
       Assertions.assertEquals(1, filmController.findAll().size());
    }

    @Test
    @DisplayName("Дата релиза — не раньше 28 декабря 1895 года.")
    void addFilmReleaseDateBeforeTheMoviesBirthday() throws RuntimeException {
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(1000, 10, 10),
                100);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    @DisplayName("Дата релиза — не раньше 28 декабря 1895 года.")
    void addFilmWithNegativeDurationFilm() throws RuntimeException {
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(2000, 10, 10),
                -100);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    @DisplayName("Изменение параметра фильма.")
    void putFilm(){
        Film film = new Film(
                1,
                "name",
                "description",
                LocalDate.of(2000, 10, 10),
                100);
        filmController.addFilm(film);
        Film film1 = new Film(
                1,
                "name1",
                "description1",
                LocalDate.of(2001, 11, 11),
                101);
        filmController.put(film1);
        Assertions.assertEquals("[Film(id=1, name=name1, " +
                "description=description1, releaseDate=2001-11-11, duration=101)]",
                filmController.findAll().toString());
    }
}
