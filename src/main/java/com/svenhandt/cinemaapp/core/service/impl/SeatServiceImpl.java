package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.SeatService;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.entity.Seat;
import com.svenhandt.cinemaapp.persistence.repository.SeatRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.Validate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    private static final Logger LOG = LoggerFactory.getLogger(SeatServiceImpl.class);

    private final char seatMarkInFile;

    private final SeatRepository seatRepository;

    public SeatServiceImpl(@Value("${cinemaapp.roomfiles.seat.mark}") char seatMarkInFile,
                           SeatRepository seatRepository) {
        this.seatMarkInFile = seatMarkInFile;
        this.seatRepository = seatRepository;
    }

    @Override
    public void createSeats(Room room, List<String> seatArrangementLines) {
        Validate.notNull(room, "Room must not be null");
        Validate.notEmpty(seatArrangementLines, "Seats must not be empty");
        List<Seat> seatsToSave = new ArrayList<>();
        for(int i = 1; i <= seatArrangementLines.size(); i++) {
            int seatRow = i;
            String seatLine = seatArrangementLines.get(i - 1);
            seatsToSave.addAll(createSeatsForSeatRow(room, seatRow, seatLine));
        }
        seatRepository.saveAll(seatsToSave);
    }

    @Override
    public List<Seat> getSeatsByRoomOrderedByRowAndNumber(Room room) {
        return seatRepository.findByRoomOrderedBySeats(room);
    }

    private List<Seat> createSeatsForSeatRow(Room room, int seatRow, String seatLine) {
        List<Seat> seatsToSave = new ArrayList<>();
        int seatNumberVisibleToVisitor = 0;
        for(int i = 1; i <= StringUtils.length(seatLine); i++) {
            char charInSeatLine = seatLine.charAt(i - 1);
            if(seatMarkInFile == charInSeatLine) {
                seatNumberVisibleToVisitor += 1;
                seatsToSave.add(createSeat(room, seatRow, i, seatNumberVisibleToVisitor));
            }
        }
        return seatsToSave;
    }

    private Seat createSeat(Room room, int seatRow, int seatNumber, int seatNumberVisibleToVisitor) {
        String seatId = "%d_seat_%d_%s".formatted(room.getId(), seatRow, seatNumber);
        LOG.info("creating seat {}", seatId);
        Seat seat = new Seat();
        seat.setId(seatId);
        seat.setSeatRow(seatRow);
        seat.setSeatNumber(seatNumber);
        seat.setSeatInfo("Reihe %d, Platz %d".formatted(seatRow, seatNumberVisibleToVisitor));
        seat.setRoom(room);
        return seat;
    }

}
