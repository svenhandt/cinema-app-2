package com.svenhandt.cinemaapp.core.converter.impl;

import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.core.dto.PresentationDto;
import com.svenhandt.cinemaapp.core.dto.PresentationsPerDayDto;
import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PresentationsOverviewConverterImplTest {

    private PresentationsOverviewConverterImpl converterImpl;

    @BeforeEach
    void setup() {
        converterImpl = new PresentationsOverviewConverterImpl(new PresentationStartTimeConverter());
    }

    @Test
    void shouldConvertCorrectlyToDtos() {
        List<FilmDto> actualFilmDtos = converterImpl.getPresentationsOverview(getSamplePresentations());
        assertThat(actualFilmDtos).isNotNull();
        assertThat(actualFilmDtos).containsExactlyElementsOf(getExpectedFilmDtos());
    }

    @Test
    void shouldThrowExceptionAtPresentationsListNull() {
        assertThatThrownBy(() -> converterImpl.getPresentationsOverview(null)).isInstanceOf(NullPointerException.class);
    }

    private List<Presentation> getSamplePresentations() {
        Film film1 = getSampleFilm(1, "Film 1");
        Film film2 = getSampleFilm(2, "Film 2");
        return List.of(
                getSamplePresentation(film1, LocalDateTime.of(2024, 12, 16, 15, 0, 0)),
                getSamplePresentation(film1, LocalDateTime.of(2024, 12, 17, 16, 0, 0)),
                getSamplePresentation(film1, LocalDateTime.of(2024, 12, 18, 17, 0, 0)),
                getSamplePresentation(film1, LocalDateTime.of(2024, 12, 19, 18, 0, 0)),
                getSamplePresentation(film1, LocalDateTime.of(2024, 12, 20, 19, 0, 0)),
                getSamplePresentation(film2, LocalDateTime.of(2024, 12, 16, 15, 0, 0)),
                getSamplePresentation(film2, LocalDateTime.of(2024, 12, 17, 16, 0, 0)),
                getSamplePresentation(film2, LocalDateTime.of(2024, 12, 18, 17, 0, 0)),
                getSamplePresentation(film2, LocalDateTime.of(2024, 12, 19, 18, 0, 0)),
                getSamplePresentation(film2, LocalDateTime.of(2024, 12, 20, 19, 0, 0))
        );
    }

    private Film getSampleFilm(int filmId, String filmName) {
        Film film = new Film();
        film.setId(filmId);
        film.setName(filmName);
        return film;
    }

    private Presentation getSamplePresentation(Film film, LocalDateTime startTime) {
        Presentation presentation = new Presentation();
        presentation.setFilm(film);
        presentation.setStartTime(startTime);
        presentation.setPrice(BigDecimal.valueOf(7.0));
        return presentation;
    }

    private List<FilmDto> getExpectedFilmDtos() {
        return List.of(
                getExpectedFilmDtoForFilm(1, "Film 1"),
                getExpectedFilmDtoForFilm(2, "Film 2")
        );
    }

    private FilmDto getExpectedFilmDtoForFilm(int id, String name) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(id);
        filmDto.setName(name);
        filmDto.setPresentationsPerDay(getExpectedPresentationsPerDayDtos());
        return filmDto;
    }

    private List<PresentationsPerDayDto> getExpectedPresentationsPerDayDtos() {
        return List.of(
                getExpectedPresentationForDay("Mo", "15:00"),
                getExpectedPresentationForDay("Di", "16:00"),
                getExpectedPresentationForDay("Mi", "17:00"),
                getExpectedPresentationForDay("Do", "18:00"),
                getExpectedPresentationForDay("Fr", "19:00")
        );
    }

    private PresentationsPerDayDto getExpectedPresentationForDay(String weekDayAsStr, String startTime) {
        PresentationsPerDayDto presentationsPerDayDto = new PresentationsPerDayDto();
        presentationsPerDayDto.setDayOfWeek(weekDayAsStr);
        PresentationDto presentationDto = new PresentationDto();
        presentationDto.setStartTime(startTime);
        presentationDto.setPrice(BigDecimal.valueOf(7.0));
        presentationsPerDayDto.setPresentations(List.of(presentationDto));
        return presentationsPerDayDto;
    }

}