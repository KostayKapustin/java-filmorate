package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer idUser = 1;

    @Override
    public Collection<User> findAll() {
        log.info("Получен запрос User, возвращающий список пользователей.");
        return users.values();
    }

    @Override
    public User create(User user) {
        validate(user);
        user.setId(idUser);
        users.put(idUser, user);
        idUser++;
        log.info("Добавлен user: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Пользователь с id %d не найден", user.getId()));
        }
        validate(user);
        log.info("Обновляется старый вариант user: {}", users.get(user.getId()));
        users.put(user.getId(), user);
        log.info("Обновленный вариант user: {}", user);
        return user;
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id))
            throw new ValidationException("Пользователя с id - " + id + " не существует.");
        log.info("Получен запрос о пользователе {} .", users.get(id));
        return users.get(id);
    }

    @Override
    public Map<Integer, User> getUsers() {
        log.info("Получен запрос о всех пользователях.");
        return users;
    }

    public void validate(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
