package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.entity.Seat;
import com.svenhandt.cinemaapp.persistence.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepositoryMock;

    @Captor
    private ArgumentCaptor<List<Seat>> seatCaptor;

    private SeatServiceImpl seatServiceImpl;

    @BeforeEach
    void setup() {
        char seatMarkInFile = 'X';
        seatServiceImpl = new SeatServiceImpl(seatMarkInFile, seatRepositoryMock);
    }

    @Test
    void shouldSuccessfullyCreateSeats() {
        List<String> seatFormationAsStringList = getSeatFormationAsStringList();
        Room roomForTest = getRoomForTest();
        List<Seat> expectedSeats = getExpectedSeats(roomForTest);
        seatServiceImpl.createSeats(roomForTest, seatFormationAsStringList);
        verify(seatRepositoryMock).saveAll(seatCaptor.capture());
        List<Seat> actualSeats = seatCaptor.getValue();
        assertThat(actualSeats).containsExactlyElementsOf(expectedSeats);
    }

    @Test
    void shouldThrowExceptionDueToNullRoom() {
        List<String> seatFormationAsStringList = getSeatFormationAsStringList();
        assertThatThrownBy(() -> seatServiceImpl.createSeats(null, seatFormationAsStringList))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionDueToEmptySeats() {
        List<String> emptySeatFormationList = Collections.emptyList();
        assertThatThrownBy(() -> seatServiceImpl.createSeats(null, emptySeatFormationList))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<String> getSeatFormationAsStringList() {
        return List.of(
                "   XXXX  ",
                " XXXXXXXX",
                "XXXXXXXXXX"
        );
    }

    private Room getRoomForTest() {
        Room room = new Room();
        room.setId(1);
        room.setName("Saal 1");
        return room;
    }

    private List<Seat> getExpectedSeats(Room room) {
        return List.of(
                getExpectedSeat("1_seat_1_4", 1 ,4, room ),
                getExpectedSeat("1_seat_1_5", 1 ,5, room ),
                getExpectedSeat("1_seat_1_6", 1 ,6, room ),
                getExpectedSeat("1_seat_1_7", 1 ,7, room ),
                getExpectedSeat("1_seat_2_2", 2 ,2, room ),
                getExpectedSeat("1_seat_2_3", 2 ,3, room ),
                getExpectedSeat("1_seat_2_4", 2 ,4, room ),
                getExpectedSeat("1_seat_2_5", 2 ,5, room ),
                getExpectedSeat("1_seat_2_6", 2 ,6, room ),
                getExpectedSeat("1_seat_2_7", 2 ,7, room ),
                getExpectedSeat("1_seat_2_8", 2 ,8, room ),
                getExpectedSeat("1_seat_2_9", 2 ,9, room ),
                getExpectedSeat("1_seat_3_1", 3 ,1, room ),
                getExpectedSeat("1_seat_3_2", 3 ,2, room ),
                getExpectedSeat("1_seat_3_3", 3 ,3, room ),
                getExpectedSeat("1_seat_3_4", 3 ,4, room ),
                getExpectedSeat("1_seat_3_5", 3 ,5, room ),
                getExpectedSeat("1_seat_3_6", 3 ,6, room ),
                getExpectedSeat("1_seat_3_7", 3 ,7, room ),
                getExpectedSeat("1_seat_3_8", 3 ,8, room ),
                getExpectedSeat("1_seat_3_9", 3 ,9, room ),
                getExpectedSeat("1_seat_3_10", 3 ,10, room )
        );
    }

    private Seat getExpectedSeat(String id, int seatRow, int seatNumber, Room room) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setSeatRow(seatRow);
        seat.setSeatNumber(seatNumber);
        seat.setRoom(room);
        return seat;
    }

}