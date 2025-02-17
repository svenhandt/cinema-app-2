package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomDto {

    private int id;

    private String name;

    private List<SeatRowDto> seatRows;

}
