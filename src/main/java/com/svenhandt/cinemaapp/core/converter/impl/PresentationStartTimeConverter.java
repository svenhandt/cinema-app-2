package com.svenhandt.cinemaapp.core.converter.impl;


import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
class PresentationStartTimeConverter {

    private static final Map<DayOfWeek, String> weekDayToStringMapping =
            Map.of(DayOfWeek.MONDAY, "Mo",
                    DayOfWeek.TUESDAY, "Di",
                    DayOfWeek.WEDNESDAY, "Mi",
                    DayOfWeek.THURSDAY, "Do",
                    DayOfWeek.FRIDAY, "Fr",
                    DayOfWeek.SATURDAY, "Sa",
                    DayOfWeek.SUNDAY, "So");

    String getDayOfWeekAsStrFormatted(Presentation presentation) {
        LocalDateTime startTime = presentation.getStartTime();
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        return weekDayToStringMapping.get(dayOfWeek);
    }

    String getStartTimeAsStrFormatted(Presentation presentation) {
        LocalDateTime startTime = presentation.getStartTime();
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
