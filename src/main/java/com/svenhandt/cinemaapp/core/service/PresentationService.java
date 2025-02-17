package com.svenhandt.cinemaapp.core.service;

import com.svenhandt.cinemaapp.persistence.entity.Presentation;

import java.util.List;

public interface PresentationService {

    void initPresentations();

    List<Presentation> getPresentationsForCurrentWeek();

    Presentation getPresentationById(int id);
}
