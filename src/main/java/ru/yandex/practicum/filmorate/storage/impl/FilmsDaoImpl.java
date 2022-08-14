package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmsDao;

@Component
public class FilmsDaoImpl implements FilmsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film saveFilm(Film film) {

        String sqlQuery =
                "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";
        String selectSql = "SELECT film_id FROM FILMS WHERE FILM_NAME = ?";
        jdbcTemplate.update(sqlQuery, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, film.getName());
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("film_id");
        }
        film.setId(id);
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlQuery = "SELECT * " +
                "FROM FILMS " +
                "WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            Film film = new Film(filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releaseDate").toLocalDate(),
                    filmRows.getInt("duration"));
            film.setId(filmRows.getInt("film_id"));
            film.setMpa(new Mpa(filmRows.getInt("mpa_id")));
            return film;
        } else {
            throw new ValidationException("Фильма с ID: " + id + " не существует!");
        }
    }

    @Override
    public void deleteFilm(Integer id) {
        String sqlQuery = "DELETE " +
                "FROM FILMS " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
