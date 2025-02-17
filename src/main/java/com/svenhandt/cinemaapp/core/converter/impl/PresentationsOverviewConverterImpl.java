package com.svenhandt.cinemaapp.core.converter.impl;

import com.svenhandt.cinemaapp.core.converter.PresentationsOverviewConverter;
import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.core.dto.PresentationDto;
import com.svenhandt.cinemaapp.core.dto.PresentationsPerDayDto;
import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PresentationsOverviewConverterImpl implements PresentationsOverviewConverter {

    private final PresentationStartTimeConverter presentationStartTimeConverter;

    PresentationsOverviewConverterImpl(PresentationStartTimeConverter presentationStartTimeConverter) {
        this.presentationStartTimeConverter = presentationStartTimeConverter;
    }

    @Override
    public List<FilmDto> getPresentationsOverview(List<Presentation> presentations) {
        Validate.notNull(presentations, "Presentations must not be null");
        List<FilmDto> filmDtos = new ArrayList<>();
        for(Presentation presentation : presentations) {
            convertAndAddToPresentationView(presentation, filmDtos);
        }
        return Collections.unmodifiableList(filmDtos);
    }

    private void convertAndAddToPresentationView(Presentation presentation,  List<FilmDto> filmDtos) {
        FilmDto filmDtoForPresentation = getFilmDtoForPresentation(presentation, filmDtos);
        PresentationsPerDayDto presentationsPerDayDto = getPresentationsPerDayDto(presentation, filmDtoForPresentation);
        addPresentationToPresentationsPerDay(presentation, presentationsPerDayDto);
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
        List<PresentationsPerDayDto> presentationsPerDayDtoList = getForFilmDto(filmDto);
        String dayOfWeekAsStr = presentationStartTimeConverter.getDayOfWeekAsStrFormatted(presentation);
        Optional<PresentationsPerDayDto> presentationsPerDayDtoOpt = presentationsPerDayDtoList.
                stream().
                filter(presentationsPerDayDto -> dayOfWeekAsStr.equals(presentationsPerDayDto.getDayOfWeek())).findFirst();
        return presentationsPerDayDtoOpt.orElseGet(() -> createPresentationsPerDayDto(dayOfWeekAsStr, presentationsPerDayDtoList));
    }

    private List<PresentationsPerDayDto> getForFilmDto(FilmDto filmDto) {
        List<PresentationsPerDayDto> presentationsPerDayList = filmDto.getPresentationsPerDay();
        if(presentationsPerDayList == null) {
            presentationsPerDayList = new ArrayList<>();
            filmDto.setPresentationsPerDay(presentationsPerDayList);
        }
        return presentationsPerDayList;
    }

    private PresentationsPerDayDto createPresentationsPerDayDto(String dayOfWeekAsStr, List<PresentationsPerDayDto> presentationsPerDayList) {
        PresentationsPerDayDto presentationsPerDayDto = new PresentationsPerDayDto();
        presentationsPerDayDto.setDayOfWeek(dayOfWeekAsStr);
        presentationsPerDayDto.setPresentations(new ArrayList<>());
        presentationsPerDayList.add(presentationsPerDayDto);
        return presentationsPerDayDto;
    }

    private void addPresentationToPresentationsPerDay(Presentation presentation, PresentationsPerDayDto presentationsPerDayDto) {
        PresentationDto presentationDto = new PresentationDto();
        presentationDto.setId(presentation.getId());
        presentationDto.setPrice(presentation.getPrice());
        presentationDto.setStartTime(presentationStartTimeConverter.getStartTimeAsStrFormatted(presentation));
        presentationsPerDayDto.getPresentations().add(presentationDto);
    }

}
