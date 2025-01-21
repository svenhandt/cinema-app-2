package com.svenhandt.cinemaapp.core.converter;

import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;

import java.util.List;

public interface PresentationsOverviewConverter {
    List<FilmDto> getPresentationsOverview(List<Presentation> presentations);
}
