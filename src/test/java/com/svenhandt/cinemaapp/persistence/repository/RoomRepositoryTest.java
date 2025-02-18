package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RoomRepositoryTest {

    private final String roomName = "Room 1";

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setup() {
        Room room = new Room();
        room.setName(roomName);
        roomRepository.save(room);
    }

    @Test
    void shouldFindRoomByName() {
        Optional<Room> actualRoomOpt = roomRepository.findByName(roomName);
        assertThat(actualRoomOpt).isPresent();
        Room actualRoom = actualRoomOpt.get();
        assertThat(actualRoom.getName()).isEqualTo(roomName);
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
    }
}