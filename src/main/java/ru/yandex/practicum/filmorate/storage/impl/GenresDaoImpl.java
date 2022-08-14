package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.dao.GenresDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenresDaoImpl implements GenresDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveGenres(Film film) {
        String sqlQuery = "INSERT INTO GENRESLIST (film_id, genre_id) " +
                "VALUES (" + film.getId() + ", ?)";
        List<Genres> genres = film.getGenres();
        if (genres != null) {
            Set<Genres> setGenres = new HashSet<>(genres);
            genres = new ArrayList<>(setGenres);
            film.setGenres(genres);
            for (Genres genre : genres) {
                jdbcTemplate.update(sqlQuery, genre.getId());
            }
        }
    }

    @Override
    public Set<Integer> getGenresById(Integer id) {
        String sqlQuery = "SELECT genre_id " +
                "FROM GENRESLIST " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> rs.getInt("genre_id")), id));
    }

    @Override
    public void deleteGenres(Integer id) {
        String sqlQuery = "DELETE " +
                "FROM GENRESLIST " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Genres getGenreById(Integer id) {
        List<Genres> allGenres = getAllGenres();
        return allGenres.stream().filter(x -> x.getId() == id).findFirst().get();
    }

    @Override
    public List<Genres> getAllGenres() {
        String sqlQuery = "SELECT * " +
                "FROM GENRE";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> getGenre(rs)));
    }

    private Genres getGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("genre_name");
        return new Genres(id, name);
    }
}
