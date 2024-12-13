package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    Optional<Film> findByName(String name);

}
