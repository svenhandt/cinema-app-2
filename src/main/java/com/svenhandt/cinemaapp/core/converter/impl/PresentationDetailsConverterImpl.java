package com.svenhandt.cinemaapp.core.converter.impl;

import com.svenhandt.cinemaapp.core.converter.PresentationDetailsConverter;
import com.svenhandt.cinemaapp.core.dto.*;
import com.svenhandt.cinemaapp.core.service.SeatService;
import com.svenhandt.cinemaapp.persistence.entity.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Component
public class PresentationDetailsConverterImpl implements PresentationDetailsConverter {

    private final PresentationStartTimeConverter presentationStartTimeConverter;
    private final SeatService seatService;

    PresentationDetailsConverterImpl(PresentationStartTimeConverter presentationStartTimeConverter, SeatService seatService) {
        this.presentationStartTimeConverter = presentationStartTimeConverter;
        this.seatService = seatService;
    }

    @Override
    public PresentationDto getPresentationDto(Presentation presentation) {
        Validate.notNull(presentation, "presentation must not be null");
        PresentationDto presentationDto = new PresentationDto();
        presentationDto.setId(presentation.getId());
        presentationDto.setPrice(presentation.getPrice());
        presentationDto.setFilmName(getFilmName(presentation));
        presentationDto.setStartTime(getStartTime(presentation));
        presentationDto.setRoom(getRoomDto(presentation));
        return presentationDto;
    }

    private String getFilmName(Presentation presentation) {
        Film film = presentation.getFilm();
        return film.getName();
    }

    private String getStartTime(Presentation presentation) {
        return "%s, %s"
                .formatted(
                        presentationStartTimeConverter.getDayOfWeekAsStrFormatted(presentation),
                        presentationStartTimeConverter.getStartTimeAsStrFormatted(presentation));
    }

    private RoomDto getRoomDto(Presentation presentation) {
        RoomDto roomDto = new RoomDto();
        Room room = presentation.getRoom();
        roomDto.setId(room.getId());
        roomDto.setName(room.getName());
        roomDto.setSeatRows(getSeatRows(presentation.getId(), room));
        return roomDto;
    }

    private List<SeatRowDto> getSeatRows(int presentationId, Room room) {
        List<Seat> seatsByRoom = seatService.getSeatsByRoomOrderedByRowAndNumber(room);
        Validate.notEmpty(seatsByRoom, "seats for room must not be empty");
        Map<Integer, List<Seat>> seatsGroupedByRows = seatsByRoom.stream().collect(groupingBy(Seat::getSeatRow));
        int maxCountOfSeatsInRow = getMaxCountOfSeatsInRow(seatsGroupedByRows);
        return getSeatRows(presentationId, seatsGroupedByRows, maxCountOfSeatsInRow);
    }

    private int getMaxCountOfSeatsInRow(Map<Integer, List<Seat>> seatsGroupedByRows) {
        return seatsGroupedByRows
                .values()
                .stream()
                .mapToInt(List::size)
                .max()
                .orElseThrow(() -> new IllegalStateException("Illegal system state: Max count of Seats in row must be present!"));
    }

    private List<SeatRowDto> getSeatRows(int presentationId, Map<Integer, List<Seat>> seatsGroupedByRows, int maxCountOfSeatsInRow) {
        List<SeatRowDto> seatRowDtos = new ArrayList<>();
        for(Map.Entry<Integer, List<Seat>> seatsGroupedByRowsEntry : seatsGroupedByRows.entrySet()) {
            SeatRowDto seatRowDto = new SeatRowDto();
            seatRowDto.setRow(seatsGroupedByRowsEntry.getKey());
            seatRowDto.setSeats(getSeatDtos(presentationId, seatsGroupedByRowsEntry.getValue(), maxCountOfSeatsInRow));
            seatRowDtos.add(seatRowDto);
        }
        return Collections.unmodifiableList(seatRowDtos);
    }

    private List<SeatDto> getSeatDtos(int presentationId, List<Seat> seats, int maxCountOfSeatsInRow) {
        List<SeatDto> seatDtos = seats
                .stream()
                .map(seat -> getSeatDto(presentationId, seat))
                .toList();
        return fillWithEmptySpaceSeatsForView(seatDtos, maxCountOfSeatsInRow);
    }

    private SeatDto getSeatDto(int presentationId, Seat seat) {
        SeatDto seatDto = new SeatDto();
        seatDto.setId(seat.getId());
        seatDto.setSeatNumber(seat.getSeatNumber());
        seatDto.setSeatInfo(seat.getSeatInfo());
        seatDto.setStatus(getSeatDtoStatus(presentationId, seat));
        return seatDto;
    }

    private SeatDtoStatus getSeatDtoStatus(int presentationId, Seat seat) {
        boolean hasBookingWithPresentation = hasBookingsWithPresentation(presentationId, seat);
        if(hasBookingWithPresentation) {
            return SeatDtoStatus.OCCUPIED;
        }
        else {
            return SeatDtoStatus.AVAILABLE;
        }
    }

    private boolean hasBookingsWithPresentation(int presentationId, Seat seat) {
        List<Booking> bookingsForSeat = ListUtils.emptyIfNull(seat.getBookings());
        return bookingsForSeat
                .stream()
                .map(Booking::getPresentation)
                .anyMatch(presentation -> presentation.getId() == presentationId);
    }

    private List<SeatDto> fillWithEmptySpaceSeatsForView(List<SeatDto> seatDtos, int maxCountOfSeatsInRow) {
        int numberOfFirstSeatInRow = seatDtos.get(0).getSeatNumber();
        int numberOfLastSeatInRow = seatDtos.get(seatDtos.size() - 1).getSeatNumber();
        List<SeatDto> mutableListOfSeatDtos = new ArrayList<>(seatDtos);
        fillWithEmptySpaceSeats(mutableListOfSeatDtos, 1, numberOfFirstSeatInRow - 1);
        fillWithEmptySpaceSeats(mutableListOfSeatDtos, numberOfLastSeatInRow + 1, maxCountOfSeatsInRow);
        return Collections.unmodifiableList(mutableListOfSeatDtos);
    }

    private void fillWithEmptySpaceSeats(List<SeatDto> mutableListOfSeatDtos, int beginIndex, int endIndex) {
        for(int i = beginIndex; i <= endIndex; i++) {
            SeatDto seatDto = new SeatDto();
            seatDto.setId(StringUtils.EMPTY);
            seatDto.setSeatNumber(i);
            seatDto.setSeatInfo(StringUtils.EMPTY);
            seatDto.setStatus(SeatDtoStatus.EMPTY_SPACE);
            mutableListOfSeatDtos.add(i - 1, seatDto);
        }
    }

}
