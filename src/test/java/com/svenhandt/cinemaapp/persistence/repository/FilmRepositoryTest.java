package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Film;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmRepositoryTest {

    private final String filmName = "Testfilm";

    @Autowired
    private FilmRepository filmRepository;

    @BeforeEach
    void setup() {
        Film film = new Film();
        film.setName(filmName);
        filmRepository.save(film);
    }

    @Test
    void shouldFindFilmByName() {
        Optional<Film> foundFilmOpt = filmRepository.findByName(filmName);
        assertThat(foundFilmOpt.isPresent()).isTrue();
        Film foundFilm = foundFilmOpt.get();
        assertThat(foundFilm.getName()).isEqualTo(filmName);
    }

    @AfterEach
    void tearDown() {
        filmRepository.deleteAll();
    }
}