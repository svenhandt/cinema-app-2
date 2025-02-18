package com.svenhandt.cinemaapp.integrationtests;

import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.entity.Seat;
import com.svenhandt.cinemaapp.persistence.repository.FilmRepository;
import com.svenhandt.cinemaapp.persistence.repository.PresentationRepository;
import com.svenhandt.cinemaapp.persistence.repository.RoomRepository;
import com.svenhandt.cinemaapp.persistence.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VerifyFileImportsIntegrationTest {

    private static final String TEST_FILM_NAME = "Pumuckl";

    private static final int EXPECTED_ROOMS_COUNT = 3;
    private static final int SEATS_COUNT_ROOM_1 = 13;
    private static final int SEATS_COUNT_ROOM_2 = 9;
    private static final int SEATS_COUNT_ROOM_3 = 36;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PresentationRepository presentationRepository;

    @Test
    void verifyPresenceOfFilms() {
        Optional<Film> expectedFilm = filmRepository.findByName(TEST_FILM_NAME);
        assertThat(expectedFilm).isPresent();
    }

    @Test
    void verifyPresenceOfRoomsAndSeats() {
        List<Room> expectedRooms = roomRepository.findAll();
        assertThat(expectedRooms).isNotEmpty();
        assertThat(expectedRooms).hasSize(EXPECTED_ROOMS_COUNT);
        Room room1 = expectedRooms.get(0);
        Room room2 = expectedRooms.get(1);
        Room room3 = expectedRooms.get(2);
        List<Seat> seatsForRoom1 = seatRepository.findByRoomOrderedBySeats(room1);
        List<Seat> seatsForRoom2 = seatRepository.findByRoomOrderedBySeats(room2);
        List<Seat> seatsForRoom3 = seatRepository.findByRoomOrderedBySeats(room3);
        assertThat(seatsForRoom1.size()).isEqualTo(SEATS_COUNT_ROOM_1);
        assertThat(seatsForRoom2.size()).isEqualTo(SEATS_COUNT_ROOM_2);
        assertThat(seatsForRoom3.size()).isEqualTo(SEATS_COUNT_ROOM_3);
    }

    @Test
    void verifyPresenceOfPresentations() {
        List<Presentation> expectedPresentations = presentationRepository.findAll();
    }


}
