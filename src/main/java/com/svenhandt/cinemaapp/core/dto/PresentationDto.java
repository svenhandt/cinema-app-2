package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PresentationDto {

    private int id;

    private String filmName;

    private String startTime;

    private BigDecimal price;

    private RoomDto room;

}
