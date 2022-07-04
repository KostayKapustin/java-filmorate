package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
        import java.util.HashMap;
        import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private LocalDate data = LocalDate.of(1895, 12, 28);
    private Integer idFilm = 1;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос Film.");
        return films.values();
    }
    @PostMapping
    public Film create(@RequestBody Film film) {
        validation(film);
        film.setId(idFilm);
        films.put(idFilm, film);
        idFilm++;
        log.info("Добавлен film: " + film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с " + film.getId() + " не найден.");
        }
        validation(film);
        log.info("Обновляется старый вариант film: {}", films.get(film.getId()));
        films.remove(film.getId());
        films.put(film.getId(), film);
        log.info("Обновленный вариант film: {}", film);
        return film;
    }
    public void validation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new InvalidEmailException("Название фильма не может быть пустым.");
        }
        if (films.containsKey(film.getName())) {
            throw new UserAlreadyExistException("Название фильма " +
                    film.getName() + " уже добавлено.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть длинее 200 симвалов.");
        }
        int result = data.compareTo(film.getReleaseDate());
        if (result > 0) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}