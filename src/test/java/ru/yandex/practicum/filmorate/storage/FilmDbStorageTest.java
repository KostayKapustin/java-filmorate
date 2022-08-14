package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {

    private FilmDbStorage filmDbStorage;

    @Autowired
    public FilmDbStorageTest(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    private Film createFirstFilm() {
        Film film = new Film( "Film1",
                "Film1",
                LocalDate.of(2000, 10, 30),
                120);
        film.setMpa(new Mpa(1));
        return film;
    }

    private Film createSecondFilm() {
        Film film2 = new Film( "Film2",
                "Film2",
                LocalDate.of(2000, 10, 30),
                120);
        film2.setMpa(new Mpa(1));
        return film2;
    }

    @Test
    void findFilmByIdTest() {
        Film film = filmDbStorage.create(createFirstFilm());
        Optional<Film> optionalFilm = Optional.ofNullable(filmDbStorage.getFilm(film.getId()));
        assertNotNull(optionalFilm);
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "FilmOne"));
        filmDbStorage.delete(film.getId());
    }

    @Test
    void updateFilmTest() {
        Film filmUpdate = createFirstFilm();
        filmDbStorage.create(filmUpdate);
        Film filmUpdate2 = createSecondFilm();
        filmUpdate2.setId(filmUpdate.getId());
        filmDbStorage.update(filmUpdate2);
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilm(filmUpdate.getId()));
        assertNotNull(filmOptional);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "Film2")
                );
        filmDbStorage.delete(filmUpdate2.getId());
    }

    @Test
    void deleteFilmTest() {
        Film filmDelete = createFirstFilm();
        filmDbStorage.create(filmDelete);
        Integer filmDeleteId = filmDelete.getId();
        filmDbStorage.delete(filmDeleteId);
        assertThrows(ValidationException.class, () -> filmDbStorage.getFilm(filmDeleteId));
    }
}