package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.TimeCalculationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TimeCalculationServiceImpl implements TimeCalculationService {

    private static final char COLON = ':';
    private static final int TIME_OF_DAY_ARR_LENGTH = 2;
    private static final Pattern TIME_OF_DAY_UNIT_PATTERN = Pattern.compile("^\\d\\d$");

    private static final Map<String, DayOfWeek> weekDayMapping =
            Map.of("Mo", DayOfWeek.MONDAY,
                    "Di", DayOfWeek.TUESDAY,
                    "Mi", DayOfWeek.WEDNESDAY,
                    "Do", DayOfWeek.THURSDAY,
                    "Fr", DayOfWeek.FRIDAY,
                    "Sa", DayOfWeek.SATURDAY,
                    "So", DayOfWeek.SUNDAY);


    @Override
    public void calculateLocalDateTime(String weekDayAsStr, String timeOfDayAsStr) {
        validateTimeInputParameters(weekDayAsStr, timeOfDayAsStr);
        DayOfWeek dayOfWeek = Optional.ofNullable(weekDayMapping.get(weekDayAsStr)).orElseThrow(
                () -> new IllegalArgumentException("Invalid week day: " + weekDayAsStr));
        String[] timeOfDayStringArr = StringUtils.split(timeOfDayAsStr, COLON);
        validateTimeOfDayStringArr(timeOfDayStringArr);

    }

    private void validateTimeInputParameters(String weekDayAsStr, String timeOfDayAsStr) {
        Validate.notEmpty(weekDayAsStr);
        Validate.notEmpty(timeOfDayAsStr);
    }

    private void validateTimeOfDayStringArr(String[] timeOfDayStringArr) {
        Validate.isTrue(TIME_OF_DAY_ARR_LENGTH == timeOfDayStringArr.length);
        String hourStr = timeOfDayStringArr[0];
        String minuteStr = timeOfDayStringArr[1];
        validateHourAndMinuteFormat(hourStr, minuteStr);
    }

    private void validateHourAndMinuteFormat(String hourStr, String minuteStr) {
        Matcher hourStrMatcher = TIME_OF_DAY_UNIT_PATTERN.matcher(hourStr);
        Matcher minuteStrMatcher = TIME_OF_DAY_UNIT_PATTERN.matcher(minuteStr);
        Validate.isTrue(hourStrMatcher.matches());
        Validate.isTrue(minuteStrMatcher.matches());
    }

    

}
