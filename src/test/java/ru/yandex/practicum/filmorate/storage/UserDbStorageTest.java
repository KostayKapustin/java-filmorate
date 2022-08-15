package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserDbStorageTest {

    private UserDbStorage userDbStorage;

    @Autowired
    public UserDbStorageTest(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    private User createFirstUser() {
        User user = new User(
                "mail1@email.ru",
                "login1",
                LocalDate.of(2000, 10, 10));
        user.setName("name1");
        return user;
    }

    private User createSecondUser() {
        User user = new User(
                "mail2@email.ru",
                "login2",
                LocalDate.of(2000, 10, 10));
        user.setName("name2");
        return user;
    }

    private User createThirdUser() {
        User user = new User(
                "mail3@email.ru",
                "login3",
                LocalDate.of(2000, 10, 10));
        user.setName("name3");
        return user;
    }

    @Test
    void userFindByIdTest() {
        User user = userDbStorage.create(createFirstUser());
        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUser(user.getId()));
        assertNotNull(userOptional);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "name1"));
        assertThrows(ValidationException.class, () -> userDbStorage.getUser(-1));
        userDbStorage.delete(user.getId());
    }

    @Test
    void updateUserTest() {
        User user1 = createFirstUser();
        userDbStorage.create(user1);
        User user2 = createSecondUser();
        user2.setId(user1.getId());
        userDbStorage.update(user2);
        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUser(user2.getId()));
        assertNotNull(userOptional);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("name", "name2")
                );
        userDbStorage.delete(user2.getId());
    }

    @Test
    void deleteUserTest() {
        User user = createFirstUser();
        userDbStorage.create(user);
        Integer userDelete = user.getId();
        userDbStorage.delete(userDelete);
        assertThrows(ValidationException.class, () -> userDbStorage.getUser(userDelete));
    }
}