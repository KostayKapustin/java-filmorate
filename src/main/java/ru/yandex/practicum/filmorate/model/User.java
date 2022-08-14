package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Integer id;

    @Email(message = "Некорректо указон Email.")
    private final String email;

    @NotNull(message = "Логин не может быть пустым и равен null.")
    @NotBlank(message = "Логин не может содерать пробелы.")
    private final String login;

    private String name;

    @Past(message = "День рождение но может быть в будущем!")
    private final LocalDate birthday;

    @Builder.Default
    private Set<Integer> friends = new HashSet<>();

}
