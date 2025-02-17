package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

@Data
public class SeatDto {

    private String id;

    private int seatNumber;

    private SeatDtoStatus status;

}
