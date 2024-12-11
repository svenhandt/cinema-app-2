package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.FilmService;
import com.svenhandt.cinemaapp.core.service.PresentationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PresentationServiceImpl implements PresentationService {

    private final String presentationFilePath;
    private final FilmService filmService;
    private final ResourceLoader resourceLoader;

    public PresentationServiceImpl(@Value("${cinemaapp.presentationfiles.path}") String presentationFilePath,
                                   FilmService filmService, ResourceLoader resourceLoader) {
        this.presentationFilePath = presentationFilePath;
        this.filmService = filmService;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void initPresentations() {

    }

    private List<String> getPresentationLines() {
        String roomFilePath = "classpath:%s/%s".formatted(roomFilesPath, roomFileName);
        Resource roomFileResource = resourceLoader.getResource(roomFilePath);
        List<String> presentationLines = new ArrayList<>();
        return presentationLines;
    }

}
