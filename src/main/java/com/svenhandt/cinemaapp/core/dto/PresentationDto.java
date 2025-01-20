package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PresentationDto {

    private int id;

    private String startTime;

    private BigDecimal price;

}
