package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> findAll();
    User create(User user);
    User update(User user);
    User getUser(int id);
    Map<Integer, User> getUsers();
}
