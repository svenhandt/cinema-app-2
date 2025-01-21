package com.svenhandt.cinemaapp.core.facade.impl;

import com.svenhandt.cinemaapp.core.converter.PresentationsOverviewConverter;
import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.core.facade.PresentationViewFacade;
import com.svenhandt.cinemaapp.core.service.PresentationService;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresentationViewFacadeImpl implements PresentationViewFacade {

    private final PresentationService presentationService;
    private final PresentationsOverviewConverter presentationsOverviewConverter;

    public PresentationViewFacadeImpl(PresentationService presentationService, PresentationsOverviewConverter presentationsOverviewConverter) {
        this.presentationService = presentationService;
        this.presentationsOverviewConverter = presentationsOverviewConverter;
    }

    @Override
    public List<FilmDto> getFilmsWithPresentationsForCurrentWeek() {
        List<Presentation> presentationsForCurrentWeek = presentationService.getPresentationsForCurrentWeek();
        return presentationsOverviewConverter.getPresentationsOverview(presentationsForCurrentWeek);
    }

}
