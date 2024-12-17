package com.svenhandt.cinemaapp.core.service;

import com.svenhandt.cinemaapp.persistence.entity.Room;

public interface RoomService {
    void createRoomAndSeats(String roomFileName);

    Room getRoom(String roomName);
}
