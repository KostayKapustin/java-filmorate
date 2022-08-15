package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;

import java.util.HashSet;
import java.util.Set;

@Component
public class LikesDaoImpl implements LikesDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveLikes(Film film) {
        String sqlQuery = "INSERT INTO LIKESLIST (film_id, user_id) VALUES (?, ?)";
        Set<Integer> likes = film.getLikeList();
        for (Integer like : likes) {
            jdbcTemplate.update(sqlQuery, film.getId(), like);
        }
    }

    @Override
    public Set<Integer> getLikesById(Integer id) {
        String sqlQuery = "SELECT user_id " +
                "FROM LIKESLIST " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("user_id"), id));
    }

    @Override
    public void deleteLikes(Integer id) {
        String sqlQuery = "DELETE " +
                "FROM LIKESLIST " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
