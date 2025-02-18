package com.svenhandt.cinemaapp.core.facade;

import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.core.dto.PresentationDto;

import java.util.List;
import java.util.Optional;

public interface PresentationViewFacade {
    List<FilmDto> getFilmsWithPresentationsForCurrentWeek();

    PresentationDto getPresentationDetailsForId(int id);
}
