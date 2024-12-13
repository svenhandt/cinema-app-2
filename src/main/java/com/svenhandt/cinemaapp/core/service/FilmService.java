package com.svenhandt.cinemaapp.core.service;

import com.svenhandt.cinemaapp.persistence.entity.Film;

public interface FilmService {
    Film getOrCreateFilm(String filmName);
}
