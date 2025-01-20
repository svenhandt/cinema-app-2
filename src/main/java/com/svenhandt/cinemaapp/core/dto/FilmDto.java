package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class FilmDto {

    private int id;

    private String name;

    private List<PresentationsPerDayDto> presentationsPerDay;

}
