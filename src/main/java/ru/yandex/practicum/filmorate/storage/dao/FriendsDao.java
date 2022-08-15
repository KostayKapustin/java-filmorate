package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendsDao {

    void saveFriends(User user);
    Set<Integer> getFriendsById(Integer id);
    void deleteFriends(Integer id);
}
