package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
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
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLikeFilm(int filmId, int userId) {
        if (!existsFilm(filmId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильма с filmId %d не существует", filmId));
        if(!userStorage.getUsers().containsKey(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        if (filmStorage.getFilm(filmId).getLikeList().contains((long) userId))
            throw new ValidationException("Пользователь с " + userId + " уже поставил лайк этому фильму.");
        filmStorage.getFilm(filmId).getLikeList().add((long) userId);
        filmStorage.getFilm(filmId).setLike(filmStorage.getFilm(filmId).getLikeList().size());
        log.info("Пользователь {} поставил лайк фильму : {} ."
                , userStorage.getUser(userId), filmStorage.getFilm(filmId));
    }

    public void deleteLikeFilm(int filmId, int userId) {
        if (!existsFilm(filmId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Фильма с filmId %d не существует", filmId));
        if(!userStorage.getUsers().containsKey(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        if (filmStorage.getFilm(filmId).getLikeList().contains((long) userId)) {
            filmStorage.getFilm(filmId).getLikeList().remove((long) userId);
            filmStorage.getFilm(filmId).setLike(filmStorage.getFilm(filmId).getLikeList().size());
            log.info("Пользователь {} удалил лайк фильму : {} ."
                    , userStorage.getUser(userId), filmStorage.getFilm(filmId));
        } else {
            throw new ValidationException("Пользователь с " + userId + " не ставил лайк.");
        }
    }

    public List<Film> popularFilms(int count) {
        List<Film> filmList = new ArrayList<>();
        for (Integer key : filmStorage.getFilms().keySet()) {
            filmList.add(filmStorage.getFilms().get(key));
        }
        log.info("Получен запрос о {} популярных фильмов.", count);
        return filmList.stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikeList().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public boolean existsFilm(int filmId) {
        return filmStorage.getFilms().containsKey(filmId);
    }
}
