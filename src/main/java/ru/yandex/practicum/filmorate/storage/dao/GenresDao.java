package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;
import java.util.Set;

public interface GenresDao {
    void saveGenres(Film film);
    Set<Integer> getGenresById(Integer id);
    void deleteGenres(Integer id);
    Genres getGenreById(Integer id);

    List<Genres> getAllGenres();
}
