package com.svenhandt.cinemaapp.core.service;

import com.svenhandt.cinemaapp.persistence.entity.Room;

import java.util.List;

public interface SeatService {
    void createSeats(Room room, List<String> seatArrangementLines);
}