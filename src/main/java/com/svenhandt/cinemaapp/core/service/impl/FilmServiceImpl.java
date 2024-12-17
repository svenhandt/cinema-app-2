package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.FilmService;
import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.repository.FilmRepository;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {

    private static final Logger LOG = LoggerFactory.getLogger(FilmServiceImpl.class);

    private final FilmRepository filmRepository;

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Film getOrCreateFilm(String filmName) {
        Validate.notEmpty(filmName);
        Film result;
        Optional<Film> filmOpt = filmRepository.findByName(filmName);
        if(filmOpt.isPresent()) {
            LOG.info("Film with name {} already exists", filmName);
            result = filmOpt.get();
        }
        else {
            result = createFilm(filmName);
        }
        return result;
    }

    private Film createFilm(String filmName) {
        Film film = new Film();
        film.setName(filmName);
        filmRepository.save(film);
        return film;
    }

}
