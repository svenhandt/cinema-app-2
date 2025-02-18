package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.entity.Seat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SeatRepositoryTest {

    private final String roomName = "Room 1";

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @BeforeEach
    void setup() {
        Room room = createSampleRoomAndSave();
        createSampleSeatAndSave(room, "saal1_1_1", 1, 1);
        createSampleSeatAndSave(room, "saal1_1_2", 1, 2);
        createSampleSeatAndSave(room, "saal1_1_3", 1, 3);
        createSampleSeatAndSave(room, "saal1_1_4", 1, 4);
        createSampleSeatAndSave(room, "saal1_1_5", 1, 5);
        createSampleSeatAndSave(room, "saal1_1_6", 1, 6);
        createSampleSeatAndSave(room, "saal1_1_7", 1, 7);
        createSampleSeatAndSave(room, "saal1_1_8", 1, 8);
        createSampleSeatAndSave(room, "saal1_1_9", 1, 9);
        createSampleSeatAndSave(room, "saal1_1_10", 1, 10);
        createSampleSeatAndSave(room, "saal1_2_1", 2, 1);
        createSampleSeatAndSave(room, "saal1_2_2", 2, 2);
        createSampleSeatAndSave(room, "saal1_2_3", 2, 3);
        createSampleSeatAndSave(room, "saal1_2_4", 2, 4);
        createSampleSeatAndSave(room, "saal1_2_5", 2, 5);
        createSampleSeatAndSave(room, "saal1_2_6", 2, 6);
        createSampleSeatAndSave(room, "saal1_2_7", 2, 7);
        createSampleSeatAndSave(room, "saal1_2_8", 2, 8);
        createSampleSeatAndSave(room, "saal1_2_9", 2, 9);
        createSampleSeatAndSave(room, "saal1_2_10", 2, 10);
    }

    @Test
    void shouldFindSeatForRoom() {
        Optional<Room> roomOpt = roomRepository.findByName(roomName);
        assertThat(roomOpt).isPresent();
        Room room = roomOpt.get();
        List<Seat> seatsForRoom = seatRepository.findByRoomOrderedBySeats(room);
        assertThat(seatsForRoom).hasSize(20);
        assertThat(seatsForRoom).containsExactlyElementsOf(getExpectedSeats());
    }

    private Room createSampleRoomAndSave() {
        Room room = getRoom();
        roomRepository.save(room);
        return room;
    }

    private void createSampleSeatAndSave(Room room, String id, int seatRow, int seatNumber) {
        Seat seat = getSeat(room, id, seatRow, seatNumber);
        seatRepository.save(seat);
    }

    private List<Seat> getExpectedSeats() {
        Room room = getRoom(1);
        return List.of(
                getSeat(room, "saal1_1_1", 1, 1),
                getSeat(room, "saal1_1_2", 1, 2),
                getSeat(room, "saal1_1_3", 1, 3),
                getSeat(room, "saal1_1_4", 1, 4),
                getSeat(room, "saal1_1_5", 1, 5),
                getSeat(room, "saal1_1_6", 1, 6),
                getSeat(room, "saal1_1_7", 1, 7),
                getSeat(room, "saal1_1_8", 1, 8),
                getSeat(room, "saal1_1_9", 1, 9),
                getSeat(room, "saal1_1_10", 1, 10),
                getSeat(room, "saal1_2_1", 2, 1),
                getSeat(room, "saal1_2_2", 2, 2),
                getSeat(room, "saal1_2_3", 2, 3),
                getSeat(room, "saal1_2_4", 2, 4),
                getSeat(room, "saal1_2_5", 2, 5),
                getSeat(room, "saal1_2_6", 2, 6),
                getSeat(room, "saal1_2_7", 2, 7),
                getSeat(room, "saal1_2_8", 2, 8),
                getSeat(room, "saal1_2_9", 2, 9),
                getSeat(room, "saal1_2_10", 2, 10)
        );
    }

    private Room getRoom(int id) {
        Room room = getRoom();
        room.setId(id);
        return room;
    }

    private Room getRoom() {
        Room room = new Room();
        room.setName(roomName);
        return room;
    }

    private Seat getSeat(Room room, String id, int seatRow, int seatNumber) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setRoom(room);
        seat.setSeatRow(seatRow);
        seat.setSeatNumber(seatNumber);
        return seat;
    }

    @AfterEach
    void tearDown() {
        seatRepository.deleteAll();
        roomRepository.deleteAll();
    }

}