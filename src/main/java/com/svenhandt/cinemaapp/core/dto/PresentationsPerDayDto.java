package com.svenhandt.cinemaapp.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class PresentationsPerDayDto {

    private String dayOfWeek;

    private List<PresentationDto> presentations;

}
