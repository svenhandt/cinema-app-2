package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.SeatService;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    private static final Logger LOG = LoggerFactory.getLogger(SeatServiceImpl.class);

    @Value("${cinemaapp.roomfiles.seat.mark}")
    private char seatMarkInFile;

    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public void createSeats(Room room, List<String> seatArrangementLines) {
        for(int i = 1; i <= seatArrangementLines.size(); i++) {
            int seatRow = i;
            String seatLine = seatArrangementLines.get(i - 1);
            createSeatsForSeatRow(room, seatRow, seatLine);
        }
    }

    private void createSeatsForSeatRow(Room room, int seatRow, String seatLine) {
        for(int i = 1; i <= seatRow; i++) {
            char charInSeatLine = seatLine.charAt(i - 1);
            if(seatMarkInFile == charInSeatLine) {
                //
            }
        }
    }

}
