MERGE INTO genre (genre_id, GENRE_NAME)
VALUES (1, 'Комедия' );
MERGE INTO genre (genre_id, GENRE_NAME)
VALUES (2, 'Драма' );
MERGE INTO genre (genre_id, GENRE_NAME)
VALUES (3, 'Мультфильм' );
MERGE INTO genre (genre_id, GENRE_NAME)
VALUES (4, 'Триллер' );
MERGE INTO genre (genre_id, GENRE_NAME)
VALUES (5, 'Документальный' );
MERGE INTO genre (genre_id, GENRE_NAME)
VALUES (6, 'Боевик' );

MERGE INTO mpa (mpa_id, MPA_NAME)
VALUES (1, 'G');
MERGE INTO mpa (mpa_id, MPA_NAME)
VALUES (2, 'PG');
MERGE INTO mpa (mpa_id, MPA_NAME)
VALUES (3, 'PG-13');
MERGE INTO mpa (mpa_id, MPA_NAME)
VALUES (4, 'R');
MERGE INTO mpa (mpa_id, MPA_NAME)
VALUES (5, 'NC-17');