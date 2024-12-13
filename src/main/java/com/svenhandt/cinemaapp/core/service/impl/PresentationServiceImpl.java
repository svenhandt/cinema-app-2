package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.FilmService;
import com.svenhandt.cinemaapp.core.service.PresentationService;
import com.svenhandt.cinemaapp.core.service.ResourceReadingService;
import com.svenhandt.cinemaapp.persistence.entity.Film;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresentationServiceImpl implements PresentationService {

    private static final char SEMICOLON = ';';
    private static final char COMMA = ',';
    private static final char SLASH = '/';

    private static final int FILM_TO_PRESENTATION_ARR_LENGTH = 2;

    private final String presentationFilePath;
    private final FilmService filmService;
    private final ResourceReadingService resourceReadingService;

    public PresentationServiceImpl(@Value("${cinemaapp.presentationfiles.path}") String presentationFilePath,
                                   FilmService filmService, ResourceReadingService resourceReadingService) {
        this.presentationFilePath = presentationFilePath;
        this.filmService = filmService;
        this.resourceReadingService = resourceReadingService;
    }

    @Override
    public void initPresentations() {
        List<String> presentationLines = resourceReadingService
                .getLinesFromFile("classpath:%s".formatted(presentationFilePath));
        Validate.notEmpty(presentationLines, "No presentations found");
        presentationLines.forEach(this::importFilmAndPresentations);
    }

    private void importFilmAndPresentations(String presentationLine) {
        String[] filmToPresentationsArr = StringUtils.split(presentationLine, SEMICOLON);
        Validate.isTrue(FILM_TO_PRESENTATION_ARR_LENGTH == filmToPresentationsArr.length, "File line is in wrong format!");
        String filmName = filmToPresentationsArr[0];
        String presentationsForFilmAsStr = filmToPresentationsArr[1];
        Validate.notEmpty(filmName);
        Film film = filmService.getOrCreateFilm(filmName);

    }

    private void createPresentations(Film film, String presentationsForFilmAsStr) {
        String[] presentationsForFilmAsArr = StringUtils.split(presentationsForFilmAsStr, COMMA);

    }

}
