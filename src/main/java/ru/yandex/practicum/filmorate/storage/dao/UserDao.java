package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

public interface UserDao {

    User saveUser(User user);

    User getUserById(Integer id);

    void deleteUser(Integer id);
}


