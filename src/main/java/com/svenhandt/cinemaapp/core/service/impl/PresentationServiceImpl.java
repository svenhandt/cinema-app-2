package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.FilmService;
import com.svenhandt.cinemaapp.core.service.PresentationService;
import com.svenhandt.cinemaapp.core.service.ResourceReadingService;
import com.svenhandt.cinemaapp.core.service.RoomService;
import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.repository.PresentationRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PresentationServiceImpl implements PresentationService {

    private static final Logger LOG = LoggerFactory.getLogger(PresentationServiceImpl.class);

    private static final char COLON = ':';
    private static final int TIME_OF_DAY_ARR_LENGTH = 2;
    private static final Pattern TIME_OF_DAY_UNIT_PATTERN = Pattern.compile("^\\d\\d$");
    private static final Pattern PRICE_PATTERN = Pattern.compile("^\\d+\\.\\d\\d$");

    private static final char SEMICOLON = ';';
    private static final char COMMA = ',';
    private static final char SLASH = '/';

    private static final int FILM_TO_PRESENTATION_ARR_LENGTH = 2;
    private static final int PRESENTATION_DATA_SIZE = 4;

    private static final Map<String, DayOfWeek> weekDayMapping =
            Map.of("Mo", DayOfWeek.MONDAY,
                    "Di", DayOfWeek.TUESDAY,
                    "Mi", DayOfWeek.WEDNESDAY,
                    "Do", DayOfWeek.THURSDAY,
                    "Fr", DayOfWeek.FRIDAY,
                    "Sa", DayOfWeek.SATURDAY,
                    "So", DayOfWeek.SUNDAY);

    private final String presentationFilePath;
    private final FilmService filmService;
    private final ResourceReadingService resourceReadingService;
    private final RoomService roomService;
    private final PresentationRepository presentationRepository;

    public PresentationServiceImpl(@Value("${cinemaapp.presentationfiles.path}") String presentationFilePath,
                                   FilmService filmService, ResourceReadingService resourceReadingService, RoomService roomService, PresentationRepository presentationRepository) {
        this.presentationFilePath = presentationFilePath;
        this.filmService = filmService;
        this.resourceReadingService = resourceReadingService;
        this.roomService = roomService;
        this.presentationRepository = presentationRepository;
    }

    @Override
    @Transactional
    public void initPresentations() {
        List<String> presentationLines = resourceReadingService
                .getLinesFromFile("classpath:%s".formatted(presentationFilePath));
        Validate.notEmpty(presentationLines, "No presentations found");
        presentationLines.forEach(this::importFilmAndPresentations);
    }

    private void importFilmAndPresentations(String presentationLine) {
        String[] filmToPresentationsArr = StringUtils.split(presentationLine, SEMICOLON);
        Validate.isTrue(FILM_TO_PRESENTATION_ARR_LENGTH == filmToPresentationsArr.length, "File line is in wrong format!");
        String filmName = filmToPresentationsArr[0];
        String presentationsForFilmAsStr = filmToPresentationsArr[1];
        Validate.notEmpty(filmName);
        Validate.notEmpty(presentationsForFilmAsStr);
        Film film = filmService.getOrCreateFilm(filmName);
        createPresentations(film, presentationsForFilmAsStr);
    }

    private void createPresentations(Film film, String presentationsForFilmAsStr) {
        String[] presentationsForFilmAsArr = StringUtils.split(presentationsForFilmAsStr, COMMA);
        for(String presentationAsStr : presentationsForFilmAsArr) {
            String[] presentationForFilmContent = StringUtils.split(presentationAsStr, SLASH);
            Validate.isTrue(PRESENTATION_DATA_SIZE == presentationForFilmContent.length, "Presentation data is in wrong format!");
            createPresentationIfNotPresent(film, presentationForFilmContent);
        }
    }

    private void createPresentationIfNotPresent(Film film, String[] presentationForFilmContent) {
        LocalDateTime startTime = getStartTime(presentationForFilmContent);
        Room presentationRoom = getPresentationRoom(presentationForFilmContent);
        BigDecimal price = getPrice(presentationForFilmContent);
        Optional<Presentation> byFilmAndRoomAndStartTimeAndPrice = presentationRepository.findByFilmAndRoomAndStartTimeAndPrice(film, presentationRoom, startTime, price);
        if(byFilmAndRoomAndStartTimeAndPrice.isEmpty()) {
            createPresentation(film, startTime, presentationRoom, price);
        }
    }

    private void createPresentation(Film film, LocalDateTime startTime, Room presentationRoom, BigDecimal price) {
        Presentation presentation = new Presentation();
        presentation.setFilm(film);
        presentation.setStartTime(startTime);
        presentation.setRoom(presentationRoom);
        presentation.setPrice(price);
        LOG.info("creating presentation {}", presentation);
        presentationRepository.save(presentation);
    }

    private LocalDateTime getStartTime(String[] presentationForFilmContent) {
        String weekDayAsStr = presentationForFilmContent[0];
        String hourOfDayAsStr = presentationForFilmContent[1];
        return calculateLocalDateTime(weekDayAsStr, hourOfDayAsStr);
    }

    private Room getPresentationRoom(String[] presentationForFilmContent) {
        String roomName = presentationForFilmContent[2];
        return roomService.getRoom(roomName);
    }

    private BigDecimal getPrice(String[] presentationForFilmContent) {
        String priceAsStr = presentationForFilmContent[3];
        Validate.isTrue(PRICE_PATTERN.matcher(priceAsStr).matches());
        return NumberUtils.createBigDecimal(priceAsStr);
    }

    private LocalDateTime calculateLocalDateTime(String weekDayAsStr, String timeOfDayAsStr) {
        validateTimeInputParameters(weekDayAsStr, timeOfDayAsStr);
        DayOfWeek dayOfWeek = Optional.ofNullable(weekDayMapping.get(weekDayAsStr)).orElseThrow(
                () -> new IllegalArgumentException("Invalid week day: " + weekDayAsStr));
        String[] timeOfDayStringArr = StringUtils.split(timeOfDayAsStr, COLON);
        validateTimeOfDayStringArr(timeOfDayStringArr);
        int hourOfDay = Integer.parseInt(timeOfDayStringArr[0]);
        int minute = Integer.parseInt(timeOfDayStringArr[1]);
        return getNextDateTime(dayOfWeek, hourOfDay, minute);
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
        validateHourAndMinuteInCorrectRange(hourStr, minuteStr);
    }

    private void validateHourAndMinuteFormat(String hourStr, String minuteStr) {
        Matcher hourStrMatcher = TIME_OF_DAY_UNIT_PATTERN.matcher(hourStr);
        Matcher minuteStrMatcher = TIME_OF_DAY_UNIT_PATTERN.matcher(minuteStr);
        Validate.isTrue(hourStrMatcher.matches());
        Validate.isTrue(minuteStrMatcher.matches());
    }

    private void validateHourAndMinuteInCorrectRange(String hourStr, String minuteStr) {
        int hourOfDay = Integer.parseInt(hourStr);
        int minute = Integer.parseInt(minuteStr);
        Validate.isTrue(hourOfDay >= 0 && hourOfDay <= 23);
        Validate.isTrue(minute >= 0 && minute <= 59);
    }

    private LocalDateTime getNextDateTime(DayOfWeek dayOfWeek, int hourOfDay, int minute) {
        return LocalDateTime
                .now()
                .with(TemporalAdjusters.next(dayOfWeek))
                .with(ChronoField.CLOCK_HOUR_OF_DAY, hourOfDay)
                .withMinute(minute)
                .withSecond(0)
                .withNano(0);
    }

}
