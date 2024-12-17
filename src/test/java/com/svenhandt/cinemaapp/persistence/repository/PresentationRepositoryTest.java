package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PresentationRepositoryTest {

    private final LocalDateTime sampleDateTime = LocalDateTime.of(2024, 12, 17, 18, 0, 0);
    private final BigDecimal samplePrice = BigDecimal.valueOf(7.59);

    private Film sampleFilm;
    private Room sampleRoom;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PresentationRepository presentationRepository;

    @BeforeEach
    void setup() {
        createSampleFilm();
        createSampleRoom();
        createSamplePresentation();
    }

    @Test
    void shouldFindByFilmAndRoomAndStartTimeAndPrice() {
        Optional<Presentation> actualPresentationOpt = presentationRepository
                .findByFilmAndRoomAndStartTimeAndPrice(sampleFilm, sampleRoom, sampleDateTime, samplePrice);
        assertThat(actualPresentationOpt).isPresent();
        Presentation actualPresentation = actualPresentationOpt.get();
        assertThat(actualPresentation.getFilm()).isEqualTo(sampleFilm);
        assertThat(actualPresentation.getRoom()).isEqualTo(sampleRoom);
        assertThat(actualPresentation.getStartTime()).isEqualTo(sampleDateTime);
        assertThat(actualPresentation.getPrice()).isEqualTo(samplePrice);
    }

    @AfterEach
    void tearDown() {
        presentationRepository.deleteAll();
        roomRepository.deleteAll();
        filmRepository.deleteAll();
    }

    private void createSampleFilm() {
        sampleFilm = new Film();
        sampleFilm.setName("Film 1");
        filmRepository.save(sampleFilm);
    }

    private void createSampleRoom() {
        sampleRoom = new Room();
        sampleRoom.setName("Room 1");
        roomRepository.save(sampleRoom);
    }

    private void createSamplePresentation() {
        Presentation samplePresentation = new Presentation();
        samplePresentation.setFilm(sampleFilm);
        samplePresentation.setRoom(sampleRoom);
        samplePresentation.setStartTime(sampleDateTime);
        samplePresentation.setPrice(samplePrice);
        presentationRepository.save(samplePresentation);
    }

}