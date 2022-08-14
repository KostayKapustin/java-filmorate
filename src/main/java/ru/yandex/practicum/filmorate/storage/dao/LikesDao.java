package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface LikesDao {
    void saveLikes(Film film);
    Set<Integer> getLikesById(Integer id);
    void deleteLikes(Integer id);
}
