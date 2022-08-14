package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.RecurException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(Integer userId, Integer friendId) {
        if(!existsUser(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        if(!existsUser(friendId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", friendId));
        User user = getUser(userId);
        User userFriend = getUser(friendId);
        Set<Integer> friends = user.getFriends();
        if(!friends.add(userFriend.getId())) {
            throw new RecurException("Пользователь с id " + userFriend.getId() + " уже есть в списке друзей!");
        }
        userStorage.update(user);
        log.info("Пользователи {} и {} стали друзьями.",
                userStorage.getUser(userId).getEmail(),
                userStorage.getUser(friendId).getEmail());
    }

    public void deleteFriends(Integer userId, Integer friendId) {
        if(!existsUser(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", userId));
        if(!existsUser(friendId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", friendId));
        User user = getUser(userId);
        User userFriend = getUser(friendId);
        if (!user.getFriends().remove(userFriend.getId())) {
            throw new ValidationException("Пользователя с id " + userFriend.getId() + " нет в вашем списке друзей!");
        }
        userStorage.update(user);
        log.info("Пользователи {} и {} больше не друзья.",
                userStorage.getUser(userId).getEmail(),
                userStorage.getUser(friendId).getEmail());
    }

    public List<User> getListMutualFriends(Integer id, Integer otherId) {
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

    public List<User> getFriends(Integer id) {
        if(!existsUser(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", id));
        List<User> friends = new ArrayList<>();
            if (!userStorage.getUser(id).getFriends().isEmpty()) {
                for (int ids : userStorage.getUser(id).getFriends()) {
                    friends.add(userStorage.getUser(ids));
                }
            }
        log.info("Получен запрос о получении списка друзей.");
        return friends;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        if (user.getId() < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с id %d не найден", user.getId()));
        }
        return userStorage.update(user);
    }

    public User getUser(Integer id) {
        if(!existsUser(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с userId %d не существует", id));
        return userStorage.getUser(id);
    }

    public boolean existsUser(Integer userId) {
        return userStorage.getUsers().containsKey(userId);
    }
}
