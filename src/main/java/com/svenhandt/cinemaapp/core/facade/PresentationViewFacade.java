package com.svenhandt.cinemaapp.core.facade;

import com.svenhandt.cinemaapp.core.dto.FilmDto;

import java.util.List;

public interface PresentationViewFacade {
    List<FilmDto> getFilmsWithPresentationsForCurrentWeek();
}
