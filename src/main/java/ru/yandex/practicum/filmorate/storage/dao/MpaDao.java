package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface MpaDao {

    List<Mpa> getAllMpa();

    Mpa getMpa(ResultSet rs) throws SQLException;
}
