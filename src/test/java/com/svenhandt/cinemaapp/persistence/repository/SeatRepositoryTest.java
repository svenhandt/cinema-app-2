package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.entity.Seat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SeatRepositoryTest {

    private final String roomName = "Room 1";

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @BeforeEach
    void setup() {
        Room room = createSampleRoom();
        createSampleSeat(room);
    }

    @Test
    void shouldFindSeatForRoom() {
        Optional<Room> roomOpt = roomRepository.findByName(roomName);
        assertThat(roomOpt).isPresent();
        Room room = roomOpt.get();
        List<Seat> seatsForRoom = seatRepository.findByRoom(room);
        assertThat(seatsForRoom).hasSize(1);
    }

    private Room createSampleRoom() {
        Room room = new Room();
        room.setName(roomName);
        roomRepository.save(room);
        return room;
    }

    private void createSampleSeat(Room room) {
        Seat seat = new Seat();
        seat.setId("1_1");
        seat.setRoom(room);
        seat.setSeatRow(1);
        seat.setSeatNumber(1);
        seatRepository.save(seat);
    }

}