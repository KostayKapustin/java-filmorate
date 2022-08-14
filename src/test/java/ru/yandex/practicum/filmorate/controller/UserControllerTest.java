/*
package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    InMemoryUserStorage controller;

    @BeforeEach
    void clearingStorage() {
        controller.getUsers().clear();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenThereAreNoUsersInStorage() throws Exception {
        this.mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAll_shouldReturnCorrectListWithUsers_whenLeastOneUserExistsInStorage() throws Exception {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("123qwe")
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        User user2 = User.builder()
                .email("mail@mail.ru")
                .login("123qwe")
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        String json = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON));
        String json2 = objectMapper.writeValueAsString(user2);
        this.mockMvc.perform(post("/users").content(json2).contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("mail")));
    }

    @Test
    void create_throwsInvalidMailException_whenInvalidEmailPassed() throws Exception {
        User user = User.builder()
                .email("mail/mail.ru")
                .login("123qwe")
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        String json = objectMapper.writeValueAsString(user);
        MvcResult response = this.mockMvc.perform(post("/users")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Некорректо указон Email."));
    }

    @Test
    void create_throwsInvalidLoginException_whenTransferringUsernameWithSpace() throws Exception {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("")
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        String json = objectMapper.writeValueAsString(user);
        MvcResult response = this.mockMvc.perform(post("/users")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Логин не может содерать пробелы."));
    }

    @Test
    void create_throwsInvalidLoginException_whenEmptyLoginPassed() throws Exception {
        User user = User.builder()
                .email("mail@mail.ru")
                .login(null)
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        String json = objectMapper.writeValueAsString(user);
        MvcResult response = this.mockMvc.perform(post("/users")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Логин не может быть пустым и равен null."));
    }

    @Test
    void create_throwsInvalidDateException_whenEmptyDatePassed() throws Exception {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("123qwe")
                .name("mail")
                .birthday(LocalDate.of(3000, 12, 12))
                .build();
        String json = objectMapper.writeValueAsString(user);
        MvcResult response = this.mockMvc.perform(post("/users")
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("День рождение но может быть в будущем!"));
    }

    @Test
    void create_shouldReturnUserWithTheCorrectData_whenTransmittingCorrectData() throws Exception {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("123qwe")
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        String json = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.email", is("mail@mail.ru")))
                .andExpect(jsonPath("$.birthday", is("" +
                        LocalDate.of(2000, 12, 12))))
                .andExpect(jsonPath("$.login", is("123qwe")))
                .andExpect(jsonPath("$.name", is("mail")));
    }

    @Test
    void update_shouldReturnUserWithTheCorrectData_whenTransmittingTheCorrectData() throws Exception {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("123qwe")
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        User user2 = User.builder()
                .id(3)
                .email("mail@mail.ru")
                .login("123qwe123")
                .name("mail2")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        String json = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(json).contentType(MediaType.APPLICATION_JSON));
        String json2 = objectMapper.writeValueAsString(user2);
        this.mockMvc.perform(put("/users").content(json2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("123qwe123")))
                .andExpect(jsonPath("$.name", is("mail2")));
    }

    @Test
    @DisplayName("Добавления пользователя с пустым именем.")
    void create_throwsAnInvalidExceptionOfTheName_ExceptionIfAnEmptyNameIsPassed() {
        User user = User.builder()
                .email("mail@mail.ru")
                .login("123qwe")
                .name("")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        controller.create(user);
        Assertions.assertEquals("123qwe", user.getName());
    }

    @Test
    void update_throwsAnInvalidExceptionForChangingUser_ExceptionIfYouChangeANonExistentUser() {
        User user = User.builder()
                .id(1)
                .email("mail@mail.ru")
                .login("123qwe")
                .name("mail")
                .birthday(LocalDate.of(2000, 12, 12))
                .build();
        Assertions.assertThrows(ResponseStatusException.class, () -> controller.update(user));
    }
}*/
