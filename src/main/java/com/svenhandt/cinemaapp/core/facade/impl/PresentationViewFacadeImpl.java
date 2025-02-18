package com.svenhandt.cinemaapp.core.facade.impl;

import com.svenhandt.cinemaapp.core.converter.PresentationDetailsConverter;
import com.svenhandt.cinemaapp.core.converter.PresentationsOverviewConverter;
import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.core.dto.PresentationDto;
import com.svenhandt.cinemaapp.core.facade.PresentationViewFacade;
import com.svenhandt.cinemaapp.core.service.PresentationService;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PresentationViewFacadeImpl implements PresentationViewFacade {

    private final PresentationService presentationService;
    private final PresentationsOverviewConverter presentationsOverviewConverter;
    private final PresentationDetailsConverter presentationDetailsConverter;

    public PresentationViewFacadeImpl(PresentationService presentationService, PresentationsOverviewConverter presentationsOverviewConverter, PresentationDetailsConverter presentationDetailsConverter) {
        this.presentationService = presentationService;
        this.presentationsOverviewConverter = presentationsOverviewConverter;
        this.presentationDetailsConverter = presentationDetailsConverter;
    }

    @Override
    public List<FilmDto> getFilmsWithPresentationsForCurrentWeek() {
        List<Presentation> presentationsForCurrentWeek = presentationService.getPresentationsForCurrentWeek();
        return presentationsOverviewConverter.getPresentationsOverview(presentationsForCurrentWeek);
    }

    @Override
    public PresentationDto getPresentationDetailsForId(int id) {
        Presentation presentation = presentationService.getPresentationById(id);
        return presentationDetailsConverter.getPresentationDto(presentation);
    }

}
