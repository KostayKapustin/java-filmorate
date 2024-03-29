package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idFilm = 1;

    @Override
    public Collection<Film> findAll() {
        log.info("Получен запрос Film, возвращающий список фильмов.");
        return films.values();
    }

    @Override
    public Film create(Film film) {
        validate(film);
        film.setId(idFilm);
        films.put(idFilm, film);
        idFilm++;
        log.info("Добавлен film: " + film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильм с id %d не найден", film.getId()));
        }
        validate(film);
        log.info("Обновляется старый вариант film: {}", films.get(film.getId()));
        films.put(film.getId(), film);
        log.info("Обновленный вариант film: {}", film);
        return film;
    }

    @Override
    public void delete(Integer id) {
        if (!films.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Фильм с id %d не найден", id));
        log.info("Удаление фильма: {}", films.get(id));
        films.remove(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        log.info("Получен запрос о всех фильмах.");
        return films;
    }

    @Override
    public Film getFilm(Integer id) {
        if (films.containsKey(id)) {
            log.info("Получен запрос о фильме {} .", films.get(id));
            return films.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильм с id %d не найден", id));
        }
    }

    @Override
    public Genres getGenreById(Integer id) {
        return null;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return null;
    }

    @Override
    public List<Genres> getAllGenres() {
        return null;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return null;
    }

    @Override
    public boolean checkingFilm(Integer id) {
        return false;
    }

    public void validate(Film film) {
        LocalDate data = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(data)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
    }
}
