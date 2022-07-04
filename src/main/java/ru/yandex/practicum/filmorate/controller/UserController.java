package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
    public User create(@RequestBody User user) {
        validation(user);
        user.setId(idUser);
        users.put(idUser, user);
        idUser++;
        log.info("Добавлен user: {}", user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Фильм с " + user.getId() + " не найден.");
        }
        validation(user);
        log.info("Обновляется старый вариант user: {}", users.get(user.getId()));
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Обновленный вариант user: {}", user);
        return user;
    }

    public void validation(User user) {
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать символ @.");
        }
        if(users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        if(user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == ""){
            user.setName(user.getLogin());
        }
        int result = LocalDate.now().compareTo(user.getBirthday());
        if (result < 0) {
            throw  new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
