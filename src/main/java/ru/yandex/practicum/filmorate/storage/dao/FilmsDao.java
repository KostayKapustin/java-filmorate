package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmsDao {
    Film saveFilm(Film film);
    Film getFilmById(Integer id);
    void deleteFilm(Integer id);
}
