package com.svenhandt.cinemaapp.core.service.impl;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

class TimeCalculationTest {

    @Test
    void testGetDateOfNextMonday() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        localDateTime = localDateTime.
                with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .with(ChronoField.CLOCK_HOUR_OF_DAY, 15)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        System.out.println(localDateTime);
    }

    @Test
    void testGetDateOfMondayOfCurrentWeekFirstSecond() {
        LocalDateTime localDateTime = LocalDateTime.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .with(ChronoField.CLOCK_HOUR_OF_DAY, 24)
                .withMinute(LocalDateTime.MIN.getMinute())
                .withSecond(LocalDateTime.MIN.getSecond())
                .withNano(LocalDateTime.MIN.getNano());
        System.out.println(localDateTime);
    }

    @Test
    void testGetDateOfSundayOfCurrentWeekLastSecond() {
        LocalDateTime localDateTime = LocalDateTime.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .with(ChronoField.CLOCK_HOUR_OF_DAY, LocalDateTime.MAX.getHour())
                .withMinute(LocalDateTime.MAX.getMinute())
                .withSecond(LocalDateTime.MAX.getSecond())
                .withNano(LocalDateTime.MAX.getNano());
        System.out.println(localDateTime);
    }

    @Test
    void testGetLocalizedTimeAsString() {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

}