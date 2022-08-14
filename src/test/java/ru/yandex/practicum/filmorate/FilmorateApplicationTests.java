package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {

	private final UserDbStorage userDbStorage;
	private final FilmDbStorage filmDbStorage;

	private User user1 = new User(
			1,
			"login",
			"mail@email.ru",
			"name",
			LocalDate.of(2000, 10, 15));
	private Film film1 = new Film(
			1,
			"film",
			"description",
			LocalDate.of(2000, 10, 15),
			60);
	private Film film2 = new Film(
			2,
			"film",
			"description",
			LocalDate.of(2000, 10, 15),
			60);

	@Test
	public void addOrUpdateUser() {
		userDbStorage.create(user1);
		assertEquals(1, userDbStorage.getUsers().size());
		User user = userDbStorage.getUser(1);
		user.setName("test");
		userDbStorage.update(user);
		assertEquals("test", userDbStorage.getUser(1).getName());
	}

	@Test
	public void addFilm() {
		film1.setMpa(new Mpa(1, "G"));
		filmDbStorage.create(film1);
		assertEquals(1, filmDbStorage.getFilms().size());
		film2.setMpa(new Mpa(2, "PG"));
		filmDbStorage.create(film2);
		assertEquals(2, filmDbStorage.getFilms().size());
	}

	@Test
	public void addOrUpdateFilm() {
		film1.setMpa(new Mpa(1, "G"));
		filmDbStorage.create(film1);
		assertEquals(1, filmDbStorage.getFilms().size());
		assertEquals("description", filmDbStorage.getFilm(1).getDescription());
		film2.setId(1);
		film2.setMpa(new Mpa(2, "PG"));
		filmDbStorage.update(film2);
		assertEquals(1, filmDbStorage.getFilms().size());
		assertEquals("description", filmDbStorage.getFilm(1).getDescription());
	}

	@Test
	public void checkMpaDao() {
		assertEquals(5, filmDbStorage.getAllMpa().size());
		assertEquals("NC-17", filmDbStorage.getMpaById(5).getName());
		assertEquals("PG-13", filmDbStorage.getMpaById(3).getName());
	}

	@Test
	public void checkGenreDao() {
		assertEquals(6, filmDbStorage.getAllGenres().size());
		assertEquals("Драма", filmDbStorage.getGenreById(2).getName());
		assertEquals("Триллер", filmDbStorage.getGenreById(4).getName());
		assertEquals("Боевик", filmDbStorage.getGenreById(6).getName());
	}
}
