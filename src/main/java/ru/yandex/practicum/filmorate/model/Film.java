package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private Integer id;
    @NotNull(message = "Название фильма не может быть пустым.")
    @NotBlank
    private String name;
    @Size(max = 200, message = "Описание может сосотоять из максимум 200 симвалов.")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительной.")
    private Integer duration;
}

