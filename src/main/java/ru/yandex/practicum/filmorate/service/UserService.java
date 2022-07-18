package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(int userId, int friendId) {
        if(!existsUser(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        if(!existsUser(friendId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", friendId));
        userStorage.getUser(userId).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
        log.info("Пользователи {} и {} стали друзьями.",
                userStorage.getUser(userId).getEmail(),
                userStorage.getUser(friendId).getEmail());
    }

    public void deleteFriends(int userId, int friendId) {
        if(!existsUser(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        if(!existsUser(friendId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", friendId));
        userStorage.getUser(userId).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
        log.info("Пользователи {} и {} больше не друзья.",
                userStorage.getUser(userId).getEmail(),
                userStorage.getUser(friendId).getEmail());
    }

    public List<User> getListMutualFriends(int id, int otherId) {
        List<User> mutualFriends = new ArrayList<>();
        if(!existsUser(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", id));
        if(!existsUser(otherId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", otherId));
        for (Integer keyUser : userStorage.getUser(id).getFriends()) {
            for (Integer keyOther : userStorage.getUser(otherId).getFriends()){
                if (userStorage.getUser(keyUser).equals(userStorage.getUser(keyOther))){
                    mutualFriends.add(userStorage.getUser(keyOther));
                }
            }
        }
        log.info("Получен запрос о получении списка общих друзей.");
        return mutualFriends;
    }

    public List<User> getFriends(int id) {
        if(!existsUser(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", id));
        List<User> friends = new ArrayList<>();
            if (!userStorage.getUser(id).getFriends().isEmpty()) {
                for (int ids : userStorage.getUser(id).getFriends()) {
                    friends.add(userStorage.getUser(ids));
                }
                log.info("Получен запрос о получении списка друзей.");
                return friends;
            }
        log.info("Получен запрос о получении пустого списка друзей.");
        return null;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUser(int id) {
        if(!existsUser(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", id));
        return userStorage.getUser(id);
    }


    public boolean existsUser(int userId) {
        return userStorage.getUsers().containsKey(userId);
    }
}
