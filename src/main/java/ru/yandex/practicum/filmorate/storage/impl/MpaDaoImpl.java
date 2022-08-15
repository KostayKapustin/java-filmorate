package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(Integer id) {
        List<Mpa> mpaList = getAllMpa();
        return mpaList.stream().filter(x -> x.getId() == id).findFirst().get();
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * " +
                "FROM MPA";
        return jdbcTemplate.query(sqlQuery, ((rs, rowNum) -> getMpa(rs)));

    }

    @Override
    public Mpa getMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("mpa_id");
        String name = rs.getString("mpa_name");
        return new Mpa(id, name);
    }
}
