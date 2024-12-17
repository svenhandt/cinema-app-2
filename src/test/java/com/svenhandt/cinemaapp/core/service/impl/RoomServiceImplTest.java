package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.ResourceReadingService;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.repository.RoomRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private SeatServiceImpl seatServiceImplMock;

    @Mock
    private RoomRepository roomRepositoryMock;

    @Mock
    private ResourceLoader resourceLoaderMock;

    @Mock
    private Resource roomFileResourceMock;

    private RoomServiceImpl roomServiceImpl;

    private final String roomFileName = getRoomFileName();
    private final String seatsArrangementAsString = getSeatsArrangementAsString();
    private final Room expectedCreatedRoom = getExpectedCreatedRoom();
    private final List<String> expectedSeatsArrangementList = getExpectedSeatsArrangementAsList();

    @BeforeEach
    void setup() {
        String roomFilePath = "rooms";
        ResourceReadingService resourceReadingServiceImpl = new ResourceReadingServiceImpl(resourceLoaderMock);
        roomServiceImpl = new RoomServiceImpl(roomFilePath, seatServiceImplMock, roomRepositoryMock, resourceReadingServiceImpl);
    }

    @Test
    void shouldCreateRoomAndSeats() throws IOException {
        when(roomRepositoryMock.findByName(eq(expectedCreatedRoom.getName()))).thenReturn(Optional.empty());
        when(resourceLoaderMock.getResource(anyString())).thenReturn(roomFileResourceMock);
        when(roomFileResourceMock.getInputStream()).thenReturn(IOUtils.toInputStream(seatsArrangementAsString, "UTF-8"));
        ArgumentCaptor<Room> savedRoomCaptor = ArgumentCaptor.forClass(Room.class);
        ArgumentCaptor<Room> roomPassedToSeatsServiceCaptor = ArgumentCaptor.forClass(Room.class);
        ArgumentCaptor<List<String>> seatsArrangementArgumentCaptor = ArgumentCaptor.forClass(List.class);
        roomServiceImpl.createRoomAndSeats(roomFileName);
        verify(roomRepositoryMock).save(savedRoomCaptor.capture());
        verify(seatServiceImplMock).createSeats(roomPassedToSeatsServiceCaptor.capture(),
                seatsArrangementArgumentCaptor.capture());
        Room actualSavedRoom = savedRoomCaptor.getValue();
        Room actualRoomPassedToSeatsService = roomPassedToSeatsServiceCaptor.getValue();
        List<String> actualSeatsArrangementList = seatsArrangementArgumentCaptor.getValue();
        assertThat(actualSavedRoom).isNotNull();
        assertThat(actualRoomPassedToSeatsService).isNotNull();
        assertThat(actualSavedRoom.getName()).isEqualTo(expectedCreatedRoom.getName());
        assertThat(actualRoomPassedToSeatsService.getName()).isEqualTo(expectedCreatedRoom.getName());
        assertThat(actualSeatsArrangementList).containsExactlyElementsOf(expectedSeatsArrangementList);
    }

    @Test
    void shouldSkipRoomAndSeatCreationAsRoomAlreadyExists() throws IOException {
        when(roomRepositoryMock.findByName(eq(expectedCreatedRoom.getName()))).thenReturn(Optional.of(expectedCreatedRoom));
        roomServiceImpl.createRoomAndSeats(roomFileName);
        verify(resourceLoaderMock, never()).getResource(anyString());
        verify(roomFileResourceMock, never()).getInputStream();
        verify(roomRepositoryMock, never()).save(any(Room.class));
        verify(seatServiceImplMock, never()).createSeats(any(), any());
    }

    @Test
    void shouldThrowExceptionAtEmptyRoomFileName() {
        assertThatThrownBy(() -> roomServiceImpl.createRoomAndSeats(StringUtils.EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionAtNullRoomFileName() {
        assertThatThrownBy(() -> roomServiceImpl.createRoomAndSeats(null))
                .isInstanceOf(NullPointerException.class);
    }

    private String getRoomFileName() {
        return "room_1.txt";
    }

    private String getSeatsArrangementAsString() {
        StringBuilder builder = new StringBuilder();
        builder.append("  X  \n");
        builder.append(" XXX \n");
        builder.append("XXXXX");
        return builder.toString();
    }

    private List<String> getExpectedSeatsArrangementAsList() {
        return List.of(
                "  X  ",
                " XXX ",
                "XXXXX"
        );
    }

    private Room getExpectedCreatedRoom() {
        Room room = new Room();
        room.setName("Room 1");
        return room;
    }
}