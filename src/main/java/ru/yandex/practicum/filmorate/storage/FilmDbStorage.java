package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.FilmsDaoImpl;
import ru.yandex.practicum.filmorate.storage.impl.GenresDaoImpl;
import ru.yandex.practicum.filmorate.storage.impl.LikesDaoImpl;
import ru.yandex.practicum.filmorate.storage.impl.MpaDaoImpl;

import java.util.*;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;
    private FilmsDaoImpl filmsDao;
    private LikesDaoImpl likesDao;
    private GenresDaoImpl genresDao;
    private MpaDaoImpl mpaDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmsDaoImpl filmsDao,
                         LikesDaoImpl likesDao, GenresDaoImpl genresDao, MpaDaoImpl mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmsDao = filmsDao;
        this.likesDao = likesDao;
        this.genresDao = genresDao;
        this.mpaDao = mpaDao;
    }

    @Override
    public Film create(Film film) {
        Film filmSave = filmsDao.saveFilm(film);
        filmSave.setMpa(mpaDao.getMpaById(filmSave.getMpa().getId()));
        genresDao.saveGenres(filmSave);
        likesDao.saveLikes(filmSave);
        return filmSave;
    }

    @Override
    public Film update(Film film) {
        if (checkingFilm(film.getId())) {
            String sqlQuery = "UPDATE films " +
                    "SET film_name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? " +
                    "WHERE film_id = ?";
            likesDao.deleteLikes(film.getId());
            likesDao.saveLikes(film);
            genresDao.deleteGenres(film.getId());
            genresDao.saveGenres(film);
            jdbcTemplate.update(sqlQuery, film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
        } else {
            create(film);
        }
        return film;
    }

    @Override
    public Film getFilm(Integer id) {
        Film film = filmsDao.getFilmById(id);
        film.setLikeList(likesDao.getLikesById(id));
        List<Genres> genres = null;
        Set<Integer> idGenre = genresDao.getGenresById(id);
        if (idGenre.size() > 0) {
            genres = new ArrayList<>();
            for (Integer idG : idGenre) {
                genres.add(genresDao.getGenreById(idG));
            }
        }
        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        film.setGenres(genres);
        return film;
    }

    @Override
    public void delete(Integer id) {
        genresDao.deleteGenres(id);
        likesDao.deleteLikes(id);
        filmsDao.deleteFilm(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> filmMap = new HashMap<>();
        List<Integer> filmsId = jdbcTemplate.query
                ("SELECT film_id FROM FILMS", ((rs, rowNum) -> rs.getInt("film_id")));
        for (Integer id : filmsId) {
            filmMap.put(id, getFilm(id));
        }
        return filmMap;
    }

    @Override
    public Collection<Film> findAll() {
        List<Film> filmList = new ArrayList<>();
        List<Integer> filmsId = jdbcTemplate.query
                ("SELECT film_id FROM FILMS", ((rs, rowNum) -> rs.getInt("film_id")));
        for (Integer id : filmsId) {
            filmList.add(getFilm(id));
        }
        return filmList;
    }

    @Override
    public Genres getGenreById(Integer id) {
        return genresDao.getGenreById(id);
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaDao.getMpaById(id);
    }

    @Override
    public List<Genres> getAllGenres() {
        return genresDao.getAllGenres();
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    @Override
    public boolean checkingFilm(Integer id) {
        String sql = "SELECT * " +
                "FROM FILMS " +
                "WHERE film_id = ?";
        return jdbcTemplate.queryForRowSet(sql, id).next();
    }
}
