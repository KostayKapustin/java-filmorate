package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer idUser = 1;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос User.");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        user.setId(idUser);
        users.put(idUser, user);
        idUser++;
        log.info("Добавлен user: {}", user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с " + user.getId() + " не найден.");
        }
        validate(user);
        log.info("Обновляется старый вариант user: {}", users.get(user.getId()));
        users.put(user.getId(), user);
        log.info("Обновленный вариант user: {}", user);
        return user;
    }

    public void validate(User user) {
        if (user.getName() == ""){
            user.setName(user.getLogin());
        }
    }

    public Map<Integer, User> getUsers() {
        return users;
    }
}
