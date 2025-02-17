package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class SeatRowDto {

    private int row;

    private List<SeatDto> seats;

}
