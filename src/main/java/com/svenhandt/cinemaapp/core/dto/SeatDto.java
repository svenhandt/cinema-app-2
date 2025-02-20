package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

@Data
public class SeatDto {

    private String id;

    private int seatNumber;

    private String seatInfo;

    private SeatDtoStatus status;

}
