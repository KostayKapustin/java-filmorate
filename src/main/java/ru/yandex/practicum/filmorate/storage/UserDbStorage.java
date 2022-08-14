package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FriendsDaoImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDaoImpl;

import java.util.*;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage{

    private JdbcTemplate jdbcTemplate;
    private UserDaoImpl userDao;
    private FriendsDaoImpl friendsDao;

    private Integer idUser = 1;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserDaoImpl userDao, FriendsDaoImpl friendsDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.friendsDao = friendsDao;
    }


    @Override
    public Collection<User> findAll() {
        Collection<User> userList = new ArrayList<>();
        List<Integer> userId = jdbcTemplate.query("SELECT user_id " +
                "FROM USERS", ((rs, rowNum) -> rs.getInt("user_id")));
        for (Integer idU : userId) {
            userList.add(getUser(idU));
        }
        return userList;
    }

    @Override
    public User create(User user) {
        validate(user);
        User userSave = userDao.saveUser(user);
        friendsDao.saveFriends(userSave);
        return userSave;
    }

    @Override
    public User update(User user) {
        validate(user);
        if (checkingUser(user.getId())) {
            String sqlQuery = "UPDATE users " +
                    "SET email = ?, login = ?, user_name = ?, birthday = ? " +
                    "WHERE user_id = ?";
            friendsDao.deleteFriends(user.getId());
            friendsDao.saveFriends(user);
            jdbcTemplate.update(sqlQuery, user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
        } else {
            create(user);
        }
        return user;
    }

    @Override
    public User getUser(Integer id) {
        User user = userDao.getUserById(id);
        user.setFriends(friendsDao.getFriendsById(id));
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> userList = new HashMap<>();
        List<Integer> userId = jdbcTemplate.query("SELECT user_id " +
                "FROM USERS", ((rs, rowNum) -> rs.getInt("user_id")));
        for (Integer idU : userId) {
            userList.put(idU, getUser(idU));
        }
        return userList;
    }

    @Override
    public void delete(Integer id) {
        friendsDao.deleteFriends(id);
        userDao.deleteUser(id);
    }

    @Override
    public boolean checkingUser(Integer id) {
        String sqlQuery = "SELECT * " +
                "FROM USERS " +
                "WHERE user_id = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id).next();
    }

    public void validate(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
