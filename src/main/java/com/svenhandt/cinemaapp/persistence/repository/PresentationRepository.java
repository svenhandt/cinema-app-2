package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PresentationRepository extends JpaRepository<Presentation, Integer> {

    Optional<Presentation> findByFilmAndRoomAndStartTimeAndPrice(Film film, Room room, LocalDateTime startTime, BigDecimal price);

}
