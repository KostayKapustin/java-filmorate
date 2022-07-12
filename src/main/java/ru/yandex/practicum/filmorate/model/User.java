package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;



@Data
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    @Email(message = "Некорректо указон Email.")
    private String email;
    @NotNull(message = "Логин не может быть пустым.")
    @NotBlank(message = "Логин не может содерать пробелы.")
    private String login;
    private String name;
    @Past(message = "День рождение но может быть в будущем!")
    private LocalDate birthday;
}
