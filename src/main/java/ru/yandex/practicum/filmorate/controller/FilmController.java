package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer idFilm = 1;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос Film, возвращающий список фильмов.");
        return films.values();
    }
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(idFilm);
        films.put(idFilm, film);
        idFilm++;
        log.info("Добавлен film: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с " + film.getId() + " не найден.");
        }
        validate(film);
        log.info("Обновляется старый вариант film: {}", films.get(film.getId()));
        films.put(film.getId(), film);
        log.info("Обновленный вариант film: {}", film);
        return film;
    }

    public void validate(Film film) {
        LocalDate data = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(data)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
    }
    public Map<Integer, Film> getFilms() {
        return films;
    }
}