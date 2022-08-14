package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;

import java.util.HashSet;
import java.util.Set;

@Component
public class FriendsDaoImpl implements FriendsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveFriends(User user) {
        String sqlQuery = "INSERT INTO FRIENDLIST (user_id, friend_id) " +
                     "VALUES (" + user.getId() + ", ?)";
        Set<Integer> friendsId = user.getFriends();
        for (Integer friendId : friendsId) {
            jdbcTemplate.update(sqlQuery, friendId);
        }
    }

    @Override
    public Set<Integer> getFriendsById(Integer id) {
        String sqlQuery = "SELECT friend_id " +
                "FROM FRIENDLIST " +
                "WHERE user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("friend_id"), id));
    }

    @Override
    public void deleteFriends(Integer id) {
        String sqlQuery = "DELETE " +
                "FROM FRIENDLIST " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
