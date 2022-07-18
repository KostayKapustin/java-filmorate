package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Collection<Film> findAll();
    Film create(Film film);
    Film update(Film film);
    void delete(int id);
    Film getFilms(int id);
    Map<Integer, Film> getFilms();
    Film getFilm(int id);
}
