package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.repository.FilmRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceImplTest {

    private static final String FILM_NAME = "Testfilm";

    @Mock
    private FilmRepository filmRepositoryMock;

    private FilmServiceImpl filmServiceImpl;

    @BeforeEach
    void setup() {
        filmServiceImpl = new FilmServiceImpl(filmRepositoryMock);
    }

    @Test
    void shouldSuccessfullyCreateFilm() {
        when(filmRepositoryMock.findByName(eq(FILM_NAME))).thenReturn(Optional.empty());
        ArgumentCaptor<Film> filmCaptor = ArgumentCaptor.forClass(Film.class);
        filmServiceImpl.getOrCreateFilm(FILM_NAME);
        verify(filmRepositoryMock).save(filmCaptor.capture());
        Film actualFilm = filmCaptor.getValue();
        assertThat(FILM_NAME).isEqualTo(actualFilm.getName());
    }

    @Test
    void shouldCreateNoFilmAsFilmAlreadyExists() {
        when(filmRepositoryMock.findByName(eq(FILM_NAME))).thenReturn(Optional.of(getSampleFilm()));
        filmServiceImpl.getOrCreateFilm(FILM_NAME);
        verify(filmRepositoryMock, never()).save(any(Film.class));
    }

    @Test
    void shouldThrowExceptionForEmptyFilmName() {
        assertThatThrownBy(() -> filmServiceImpl.getOrCreateFilm(StringUtils.EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionForNullFilmName() {
        assertThatThrownBy(() -> filmServiceImpl.getOrCreateFilm(null))
                .isInstanceOf(NullPointerException.class);
    }


    private Film getSampleFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName(FILM_NAME);
        return film;
    }
}