package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.RecurException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLikeFilm(Integer filmId, Integer userId) {
        if (!checkingFilm(filmId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильма с filmId %d не существует", filmId));
        if (!userStorage.getUsers().containsKey(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        if (filmStorage.getFilm(filmId).getLikeList().contains(userId))
            throw new RecurException("Пользователь с " + userId + " уже поставил лайк этому фильму.");
        filmStorage.getFilm(filmId).getLikeList().add(userId);
        filmStorage.getFilm(filmId).setLike(filmStorage.getFilm(filmId).getLikeList().size());
        log.info("Пользователь {} поставил лайк фильму : {} ."
                , userStorage.getUser(userId), filmStorage.getFilm(filmId));
    }

    public void deleteLikeFilm(Integer filmId, Integer userId) {
        if (!checkingFilm(filmId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильма с filmId %d не существует", filmId));
        if (!userStorage.getUsers().containsKey(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        Film film = filmStorage.getFilm(filmId);
        if (!film.getLikeList().contains(userId)) {
            filmStorage.getFilm(filmId).getLikeList().remove(userId);
            filmStorage.getFilm(filmId).setLike(filmStorage.getFilm(filmId).getLikeList().size());
            log.info("Пользователь {} удалил лайк фильму : {} ."
                    , userStorage.getUser(userId), filmStorage.getFilm(filmId));
        } else {
            throw new ValidationException("Пользователь с id " + userId + " не ставил лайк.");
        }
    }

    public List<Film> popularFilms(Integer count) {
        log.info("Получен запрос о {} популярных фильмов.", count);
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikeList().size()).reversed())
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(Integer id) {
        if (!filmStorage.checkingFilm(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильм с id %d не найден", id));
        }
        return filmStorage.getFilm(id);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        ;
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (film.getId() < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильм с id %d не найден", film.getId()));
        }
        return filmStorage.update(film);
    }

    public Genres getGenreById(Integer id) {
        if (getAllGenres().size() < id || id < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Жанр не найден"));
        }
        return getAllGenres().stream().filter(x -> x.getId() == id).findFirst().get();
    }

    public Mpa getMpaById(Integer id) {
        if (getAllMpa().size() < id || id < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Mpa не найден"));
        }
        return getAllMpa().stream().filter(x -> x.getId() == id).findFirst().get();
    }

    public boolean checkingFilm(Integer filmId) {
        return filmStorage.getFilms().containsKey(filmId);
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public List<Genres> getAllGenres() {
        return filmStorage.getAllGenres();
    }
}
