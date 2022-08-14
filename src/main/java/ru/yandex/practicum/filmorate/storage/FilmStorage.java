package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    void delete(Integer id);

    Map<Integer, Film> getFilms();

    Film getFilm(Integer id);

    Genres getGenreById(Integer id);

    Mpa getMpaById(Integer id);

    List<Genres> getAllGenres();

    List<Mpa> getAllMpa();

    boolean checkingFilm(Integer id);
}
