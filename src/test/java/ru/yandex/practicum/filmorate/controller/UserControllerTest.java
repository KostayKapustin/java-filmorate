package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private final UserController userController = new UserController();

    @Test
    @DisplayName("Получение пустого списка всех пользователей.")
    void findAllNull() {
        Assertions.assertEquals(0, userController.findAll().size());
    }

    @Test
    @DisplayName("Получение пустого списка всех пользователей.Проверка добавления.")
    void findAll() {
        User user = new User(
                1,
                "mail@mail.ru",
                "NickName",
                "name",
                LocalDate.of(2000, 10, 10)
        );
        userController.addUser(user);
        Assertions.assertEquals(1, userController.findAll().size());
    }

    @Test
    @DisplayName("Добавления пользователя без знака<@>.")
    void addUserWithoutSing() {
        User user = new User(
                1,
                "mailmail.ru",
                "NickName",
                "name",
                LocalDate.of(2000, 10, 10)
        );
        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    @DisplayName("Добавления пользователя без mail.")
    void addUserNullMail() {
        User user = new User(
                1,
                "",
                "NickName",
                "name",
                LocalDate.of(2000, 10, 10)
        );
        Assertions.assertThrows(InvalidEmailException.class, () -> userController.addUser(user));
    }

    @Test
    @DisplayName("Добавления пользователя без логина.")
    void addUserWithoutLogin() {
        User user = new User(
                1,
                "mail@mail.ru",
                "",
                "name",
                LocalDate.of(2000, 10, 10)
        );
        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    @DisplayName("Добавления пользователя с пробелом в логине.")
    void addUserWithSpaceLogin() {
        User user = new User(
                1,
                "mail@mail.ru",
                "Nick    Name",
                "name",
                LocalDate.of(2000, 10, 10)
        );
        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    @DisplayName("Добавления пользователя с пустым именем.")
    void addUserEmptyName() {
        User user = new User(
                1,
                "mail@mail.ru",
                "NickName",
                "",
                LocalDate.of(2000, 10, 10)
        );
        userController.addUser(user);
        Assertions.assertEquals("NickName", user.getName());
    }

    @Test
    @DisplayName("Добавления пользователя с датой рожедения в будущем.")
    void addUserEmptyВateАirthFuture() {
        User user = new User(
                1,
                "mail@mail.ru",
                "NickName",
                "",
                LocalDate.of(3000, 10, 10)
        );
        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}