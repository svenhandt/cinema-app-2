package com.svenhandt.cinemaapp.presentation.controller;

import com.svenhandt.cinemaapp.core.dto.FilmDto;
import com.svenhandt.cinemaapp.core.facade.PresentationViewFacade;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class PresentationViewController {

    private final PresentationViewFacade presentationViewFacade;

    public PresentationViewController(PresentationViewFacade presentationViewFacade) {
        this.presentationViewFacade = presentationViewFacade;
    }

    @GetMapping("/presentations")
    public List<FilmDto> getPresentationsForCurrentWeek() {
        return presentationViewFacade.getFilmsWithPresentationsForCurrentWeek();
    }

}
