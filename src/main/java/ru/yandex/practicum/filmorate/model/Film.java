package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {

    private Integer id;

    @NotNull(message = "Имя не может равняться null")
    @NotBlank(message = "Название фильма не может быть пустым.")
    private final String name;

    @Size(max = 200, message = "Описание может сосотоять из максимум 200 симвалов.")
    private final String description;

    private final LocalDate releaseDate;

    @DecimalMin(value = "0", message = "Продолжительность фильма должна быть положительной и не равным текущей дате.")
    private final Integer duration;

    private Set<Integer> likeList = new HashSet<>();

    private int like;

    private List<Genres> genres;

    private Mpa mpa;

}

