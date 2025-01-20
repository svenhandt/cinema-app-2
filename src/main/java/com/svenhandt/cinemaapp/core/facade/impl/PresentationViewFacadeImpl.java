package com.svenhandt.cinemaapp.core.facade.impl;

import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.core.dto.PresentationsPerDayDto;
import com.svenhandt.cinemaapp.core.service.PresentationService;
import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PresentationViewFacadeImpl {

    private static final Map<DayOfWeek, String> weekDayToStringMapping =
            Map.of(DayOfWeek.MONDAY, "Mo",
                    DayOfWeek.TUESDAY, "Di",
                    DayOfWeek.WEDNESDAY, "Mi",
                    DayOfWeek.THURSDAY, "Do",
                    DayOfWeek.FRIDAY, "Fr",
                    DayOfWeek.SATURDAY, "Sa",
                    DayOfWeek.SUNDAY, "So");

    private final PresentationService presentationService;

    public PresentationViewFacadeImpl(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    public List<FilmDto> getFilmsWithPresentationsForCurrentWeek() {
        List<Presentation> presentationsForCurrentWeek = presentationService.getPresentationsForCurrentWeek();
        List<FilmDto> filmDtos = new ArrayList<FilmDto>();
        return filmDtos;
    }

    private void convertToPresentationsViewForWeek(List<Presentation> presentationsForCurrentWeek, List<FilmDto> filmDtos) {
        for(Presentation presentation : presentationsForCurrentWeek) {

        }
    }

    private void convertAndAddToPresentationsViewForWeek(Presentation presentation,  List<FilmDto> filmDtos) {
        FilmDto filmDtoForPresentation = getFilmDtoForPresentation(presentation, filmDtos);
    }

    private FilmDto getFilmDtoForPresentation(Presentation presentation,  List<FilmDto> filmDtos) {
        Film film = presentation.getFilm();
        Optional<FilmDto> filmDtoOpt = filmDtos
                .stream()
                .filter(f -> f.getId() == film.getId()).findFirst();
        return filmDtoOpt.orElseGet(() -> addFilmDtoAndGet(film, filmDtos));
    }

    private FilmDto addFilmDtoAndGet(Film film, List<FilmDto> filmDtos) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDtos.add(filmDto);
        return filmDto;
    }

    private PresentationsPerDayDto getPresentationsPerDayDto(Presentation presentation, FilmDto filmDto) {
        filmDto.ge
    }

    private List<PresentationsPerDayDto> getForFilmDto(FilmDto filmDto) {
        
    }

}
