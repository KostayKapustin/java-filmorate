package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> findAll();
    User create(User user);
    User update(User user);
    User getUser(Integer id);
    Map<Integer, User> getUsers();

    void delete(Integer id);

    boolean checkingUser(Integer id);
}
