package com.svenhandt.cinemaapp.core.service.impl;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;

import static org.junit.jupiter.api.Assertions.*;

class TimeCalculationServiceImplTest {

    @Test
    void testGetDateOfNextMonday() {
        LocalDateTime ld = LocalDateTime.now();
        System.out.println(ld);
        ld = ld.
                with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .with(ChronoField.CLOCK_HOUR_OF_DAY, 15)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        System.out.println(ld);
    }

}