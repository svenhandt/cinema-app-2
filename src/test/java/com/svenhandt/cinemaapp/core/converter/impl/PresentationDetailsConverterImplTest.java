package com.svenhandt.cinemaapp.core.converter.impl;

import com.svenhandt.cinemaapp.core.dto.*;
import com.svenhandt.cinemaapp.core.service.SeatService;
import com.svenhandt.cinemaapp.persistence.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresentationDetailsConverterImplTest {

    private static final String FILM_NAME_UNIT_TESTING = "Unit-Testing";
    private static final String SAAL_1 = "Saal 1";

    @Mock
    private SeatService seatService;

    private PresentationDetailsConverterImpl presentationDetailsConverter;

    @BeforeEach
    void setup() {
        presentationDetailsConverter = new PresentationDetailsConverterImpl(new PresentationStartTimeConverter(),
                seatService);
    }

    @Test
    void shouldConvertPresentationCorrectly() {
        Presentation givenPresentation = getGivenPresentation();
        when(seatService.getSeatsByRoomOrderedByRowAndNumber(eq(givenPresentation.getRoom())))
                .thenReturn(getSeatsForGivenPresentation(givenPresentation));
        PresentationDto actualPresentationDto = presentationDetailsConverter.getPresentationDto(givenPresentation);
        assertThat(actualPresentationDto).isNotNull();
        assertThat(actualPresentationDto).isEqualTo(getExpectedPresentationDto());
    }

    @Test
    void shouldThrowExceptionWhenPresentationIsNull() {
        assertThatThrownBy(() -> presentationDetailsConverter.getPresentationDto(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionIfSeatsForRoomAreEmpty() {
        Presentation givenPresentation = getGivenPresentation();
        when(seatService.getSeatsByRoomOrderedByRowAndNumber(eq(givenPresentation.getRoom())))
                .thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> presentationDetailsConverter.getPresentationDto(givenPresentation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Presentation getGivenPresentation() {
        Presentation presentation = new Presentation();
        presentation.setId(1);
        presentation.setPrice(BigDecimal.valueOf(7.0));
        presentation.setStartTime(LocalDateTime.of(2025, 2, 17, 15, 0, 0));
        presentation.setFilm(getFilmForGivenPresentation());
        presentation.setRoom(getRoomForGivenPresentation());
        return presentation;
    }

    private Film getFilmForGivenPresentation() {
        Film film = new Film();
        film.setId(1);
        film.setName(FILM_NAME_UNIT_TESTING);
        return film;
    }

    private Room getRoomForGivenPresentation() {
        Room room = new Room();
        room.setId(1);
        room.setName(SAAL_1);
        return room;
    }

    private List<Seat> getSeatsForGivenPresentation(Presentation presentation) {
        return List.of(
                getSeat("room1_1_3", 1, 3, "Reihe 1, Platz 1"),
                getSeat("room1_1_4", 1, 4, "Reihe 1, Platz 2"),
                getSeat("room1_1_5", 1, 5, "Reihe 1, Platz 3"),
                getSeat("room1_1_6", 1, 6, "Reihe 1, Platz 4",
                        createSampleBooking(1, "Owner 1", presentation)),
                getSeat("room1_2_2", 2, 2, "Reihe 2, Platz 1"),
                getSeat("room1_2_3", 2, 3, "Reihe 2, Platz 2"),
                getSeat("room1_2_4", 2, 4, "Reihe 2, Platz 3"),
                getSeat("room1_2_5", 2, 5, "Reihe 2, Platz 4"),
                getSeat("room1_2_6", 2, 6, "Reihe 2, Platz 5"),
                getSeat("room1_2_7", 2, 7, "Reihe 2, Platz 6",
                        createSampleBooking(2, "Owner 2", presentation)),
                getSeat("room1_3_1", 3, 1, "Reihe 3, Platz 1"),
                getSeat("room1_3_2", 3, 2, "Reihe 3, Platz 2"),
                getSeat("room1_3_3", 3, 3, "Reihe 3, Platz 3"),
                getSeat("room1_3_4", 3, 4, "Reihe 3, Platz 4"),
                getSeat("room1_3_5", 3, 5, "Reihe 3, Platz 5"),
                getSeat("room1_3_6", 3, 6, "Reihe 3, Platz 6"),
                getSeat("room1_3_7", 3, 7, "Reihe 3, Platz 7"),
                getSeat("room1_3_8", 3, 8, "Reihe 3, Platz 8")
        );
    }

    private Booking createSampleBooking(int id, String owner, Presentation presentation) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setOwner(owner);
        booking.setPresentation(presentation);
        return booking;
    }

    private Seat getSeat(String id, int row, int numberInRow, String seatInfo, Booking booking) {
        Seat seat = getSeat(id, row, numberInRow, seatInfo);
        seat.setBookings(List.of(booking));
        return seat;
    }

    private Seat getSeat(String id, int row, int numberInRow, String seatInfo) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setSeatRow(row);
        seat.setSeatNumber(numberInRow);
        seat.setSeatInfo(seatInfo);
        return seat;
    }

    private PresentationDto getExpectedPresentationDto() {
        PresentationDto presentationDto = new PresentationDto();
        presentationDto.setId(1);
        presentationDto.setPrice(BigDecimal.valueOf(7.0));
        presentationDto.setFilmName(FILM_NAME_UNIT_TESTING);
        presentationDto.setStartTime("Mo, 15:00");
        presentationDto.setRoom(getExpectedRoomDto());
        return presentationDto;
    }

    private RoomDto getExpectedRoomDto() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(1);
        roomDto.setName(SAAL_1);
        roomDto.setSeatRows(getExpectedSeatRowDtos());
        return roomDto;
    }

    private List<SeatRowDto> getExpectedSeatRowDtos() {
        return List.of(
                getSeatsForRow1(),
                getSeatsForRow2(),
                getSeatsForRow3()
        );
    }

    private SeatRowDto getSeatsForRow1() {
        SeatRowDto seatRowDto = new SeatRowDto();
        seatRowDto.setRow(1);
        seatRowDto.setSeats(
            List.of(
                getSeatDto(StringUtils.EMPTY, 1, StringUtils.EMPTY, SeatDtoStatus.EMPTY_SPACE),
                getSeatDto(StringUtils.EMPTY, 2, StringUtils.EMPTY, SeatDtoStatus.EMPTY_SPACE),
                getSeatDto("room1_1_3", 3, "Reihe 1, Platz 1", SeatDtoStatus.AVAILABLE),
                getSeatDto("room1_1_4", 4, "Reihe 1, Platz 2", SeatDtoStatus.AVAILABLE),
                getSeatDto("room1_1_5", 5, "Reihe 1, Platz 3", SeatDtoStatus.AVAILABLE),
                getSeatDto("room1_1_6", 6, "Reihe 1, Platz 4", SeatDtoStatus.OCCUPIED),
                getSeatDto(StringUtils.EMPTY, 7, StringUtils.EMPTY, SeatDtoStatus.EMPTY_SPACE),
                getSeatDto(StringUtils.EMPTY, 8, StringUtils.EMPTY, SeatDtoStatus.EMPTY_SPACE)
            )
        );
        return seatRowDto;
    }

    private SeatRowDto getSeatsForRow2() {
        SeatRowDto seatRowDto = new SeatRowDto();
        seatRowDto.setRow(2);
        seatRowDto.setSeats(
                List.of(
                        getSeatDto(StringUtils.EMPTY, 1, StringUtils.EMPTY, SeatDtoStatus.EMPTY_SPACE),
                        getSeatDto("room1_2_2", 2, "Reihe 2, Platz 1", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_2_3", 3, "Reihe 2, Platz 2", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_2_4", 4, "Reihe 2, Platz 3", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_2_5", 5, "Reihe 2, Platz 4", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_2_6", 6, "Reihe 2, Platz 5", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_2_7", 7, "Reihe 2, Platz 6", SeatDtoStatus.OCCUPIED),
                        getSeatDto(StringUtils.EMPTY, 8, StringUtils.EMPTY,  SeatDtoStatus.EMPTY_SPACE)
                )
        );
        return seatRowDto;
    }

    private SeatRowDto getSeatsForRow3() {
        SeatRowDto seatRowDto = new SeatRowDto();
        seatRowDto.setRow(3);
        seatRowDto.setSeats(
                List.of(
                        getSeatDto("room1_3_1", 1, "Reihe 3, Platz 1", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_3_2", 2, "Reihe 3, Platz 2", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_3_3", 3, "Reihe 3, Platz 3", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_3_4", 4, "Reihe 3, Platz 4", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_3_5", 5, "Reihe 3, Platz 5", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_3_6", 6, "Reihe 3, Platz 6", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_3_7", 7, "Reihe 3, Platz 7", SeatDtoStatus.AVAILABLE),
                        getSeatDto("room1_3_8", 8, "Reihe 3, Platz 8", SeatDtoStatus.AVAILABLE)
                )
        );
        return seatRowDto;
    }

    private SeatDto getSeatDto(String id, int number, String seatInfo, SeatDtoStatus seatDtoStatus) {
        SeatDto seatDto = new SeatDto();
        seatDto.setId(id);
        seatDto.setSeatNumber(number);
        seatDto.setSeatInfo(seatInfo);
        seatDto.setStatus(seatDtoStatus);
        return seatDto;
    }

}