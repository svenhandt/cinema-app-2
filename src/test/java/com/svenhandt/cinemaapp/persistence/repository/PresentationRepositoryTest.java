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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PresentationRepositoryTest {

    private final LocalDateTime sampleStartTime1 = LocalDateTime.of(2024, 12, 17, 18, 0, 0);
    private final LocalDateTime sampleStartTime2 = LocalDateTime.of(2024, 12, 17, 20, 0, 0);
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
        createSamplePresentations();
    }

    @Test
    void shouldFindByFilmAndRoomAndStartTimeAndPrice() {
        Optional<Presentation> actualPresentationOpt = presentationRepository
                .findByFilmAndRoomAndStartTimeAndPrice(sampleFilm, sampleRoom, sampleStartTime1, samplePrice);
        assertThat(actualPresentationOpt).isPresent();
        Presentation actualPresentation = actualPresentationOpt.get();
        assertThat(actualPresentation.getFilm()).isEqualTo(sampleFilm);
        assertThat(actualPresentation.getRoom()).isEqualTo(sampleRoom);
        assertThat(actualPresentation.getStartTime()).isEqualTo(sampleStartTime1);
        assertThat(actualPresentation.getPrice()).isEqualTo(samplePrice);
    }

    @Test
    void shouldFindPresentationsWithinTimeRange() {
        LocalDateTime timeRangeBegin = LocalDateTime.of(2024, 12, 17, 0, 0, 0);
        LocalDateTime timeRangeEnd = LocalDateTime.of(2024, 12, 17, 23, 59, 59);
        List<Presentation> foundPresentations = presentationRepository.findByStartTimeIsBetweenOrderByStartTime(timeRangeBegin, timeRangeEnd);
        assertThat(foundPresentations).isNotNull();
        assertThat(foundPresentations.size()).isEqualTo(2);
        Presentation presentationStartingEarlier = foundPresentations.get(0);
        Presentation presentationStartingLater = foundPresentations.get(1);
        assertThat(presentationStartingEarlier.getStartTime()).isEqualTo(sampleStartTime1);
        assertThat(presentationStartingLater.getStartTime()).isEqualTo(sampleStartTime2);
    }

    @Test
    void shouldReturnEmptyPresentationsList() {
        LocalDateTime timeRangeBegin = LocalDateTime.of(2024, 12, 18, 0, 0, 0);
        LocalDateTime timeRangeEnd = LocalDateTime.of(2024, 12, 18, 23, 59, 59);
        List<Presentation> foundPresentations = presentationRepository.findByStartTimeIsBetweenOrderByStartTime(timeRangeBegin, timeRangeEnd);
        assertThat(foundPresentations).isNotNull();
        assertThat(foundPresentations).isEmpty();
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

    private void createSamplePresentations() {
        createSamplePresentation(sampleStartTime1);
        createSamplePresentation(sampleStartTime2);
    }

    private void createSamplePresentation(LocalDateTime startTime) {
        Presentation samplePresentation = new Presentation();
        samplePresentation.setFilm(sampleFilm);
        samplePresentation.setRoom(sampleRoom);
        samplePresentation.setStartTime(startTime);
        samplePresentation.setPrice(samplePrice);
        presentationRepository.save(samplePresentation);
    }

}