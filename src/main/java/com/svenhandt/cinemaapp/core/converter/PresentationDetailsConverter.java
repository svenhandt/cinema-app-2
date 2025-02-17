package com.svenhandt.cinemaapp.core.converter;

import com.svenhandt.cinemaapp.core.dto.PresentationDto;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;

public interface PresentationDetailsConverter {

    PresentationDto getPresentationDto(Presentation presentation);

}
