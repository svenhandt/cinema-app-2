package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.FilmService;
import com.svenhandt.cinemaapp.core.service.ResourceReadingService;
import com.svenhandt.cinemaapp.core.service.RoomService;
import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.entity.Presentation;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.repository.PresentationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresentationServiceImplTest {

    private static final String SAAL_1 = "Saal 1";
    private static final String SAAL_2 = "Saal 2";
    private static final String SAAL_3 = "Saal 3";
    private static final int MONEY_AFTER_COMMA_DIGITS = 2;

    @Mock
    private FilmService filmServiceMock;

    @Mock
    private ResourceReadingService resourceReadingServiceMock;

    @Mock
    private RoomService roomServiceMock;

    @Mock
    private PresentationRepository presentationRepositoryMock;

    private final String presentationFilePath = "presentations/presentations.txt";
    private final List<String> correctInputPresentationLines = getCorrectInputPresentationLines();
    private final Film expectedFilm = getExpectedFilm();
    private final Room room_1 = getRoom(SAAL_1);
    private final Room room_2 = getRoom(SAAL_2);
    private final Room room_3 = getRoom(SAAL_3);
    private final List<Presentation> expectedPresentationsCompletely = getExpectedPresentationsCompletely();
    private final List<Presentation> expectedPresentationsWithoutWednesday = getExpectedPresentationsWithoutWednesday();


    private PresentationServiceImpl presentationServiceImpl;

    @BeforeEach
    void setup() {
        presentationServiceImpl = new PresentationServiceImpl(presentationFilePath,
                filmServiceMock,
                resourceReadingServiceMock,
                roomServiceMock,
                presentationRepositoryMock);
    }

    @Test
    void shouldSuccessfullyInitPresentations_completely() {
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(any(), any(), any(), any()))
                .thenReturn(Optional.empty());
        prepareCommonMocks();
        ArgumentCaptor<String> filmNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Presentation> presentationCaptor = ArgumentCaptor.forClass(Presentation.class);
        presentationServiceImpl.initPresentations();
        verifyFilm(filmNameCaptor);
        verify(presentationRepositoryMock, times(expectedPresentationsCompletely.size())).save(presentationCaptor.capture());
        List<Presentation> allValues = presentationCaptor.getAllValues();
        assertThat(allValues).hasSize(expectedPresentationsCompletely.size());
        assertThat(allValues).hasSameElementsAs(expectedPresentationsCompletely);
    }

    @Test
    void shouldSuccessfullyInitPresentations_wednesdayAlreadyPresent() {
        prepareCommonMocks();
        prepareMocks_WednesdayPresentationAlreadyAvailable();
        ArgumentCaptor<String> filmNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Presentation> presentationCaptor = ArgumentCaptor.forClass(Presentation.class);
        presentationServiceImpl.initPresentations();
        verifyFilm(filmNameCaptor);
        verify(presentationRepositoryMock, times(expectedPresentationsWithoutWednesday.size())).save(presentationCaptor.capture());
        List<Presentation> allValues = presentationCaptor.getAllValues();
        assertThat(allValues).hasSize(expectedPresentationsWithoutWednesday.size());
        assertThat(allValues).hasSameElementsAs(expectedPresentationsWithoutWednesday);
    }

    @Test
    void shouldThrowExceptionDueToCompletelyInvalidInputLine() {
        verifyExceptionThrownDueToInvalidInputLines(List.of("xxxxxxxxxxx"));
    }

    @Test
    void shouldThrowExceptionDueToInvalidInputLine_filmCorrect_presentationsInvalid() {
        verifyExceptionThrownDueToInvalidInputLines(List.of("Testfilm;xxxxxxxxxx"));
    }

    @Test
    void shouldThrowExceptionDueToInvalidInputLine_filmCorrect_presentationNotInProperFormat() {
        verifyExceptionThrownDueToInvalidInputLines(List.of("Testfilm;Pumuckl;Mo/17:00/Saal 1"));
    }

    private void verifyExceptionThrownDueToInvalidInputLines(List<String> inputLines) {
        when(resourceReadingServiceMock.getLinesFromFile(eq("classpath:%s".formatted(presentationFilePath))))
                .thenReturn(inputLines);
        assertThatThrownBy(() -> presentationServiceImpl.initPresentations()).isInstanceOf(IllegalArgumentException.class);
    }

    private void prepareCommonMocks() {
        when(resourceReadingServiceMock.getLinesFromFile(eq("classpath:%s".formatted(presentationFilePath))))
                .thenReturn(correctInputPresentationLines);
        when(filmServiceMock.getOrCreateFilm(eq(expectedFilm.getName()))).thenReturn(expectedFilm);
        when(roomServiceMock.getRoom(eq(SAAL_1))).thenReturn(room_1);
        when(roomServiceMock.getRoom(eq(SAAL_2))).thenReturn(room_2);
        when(roomServiceMock.getRoom(eq(SAAL_3))).thenReturn(room_3);
    }

    private void prepareMocks_WednesdayPresentationAlreadyAvailable() {
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(eq(expectedFilm),
                eq(room_1),
                eq(getExpectedMondayStarttime()),
                eq(getExpectedPrice(7.0))))
                .thenReturn(Optional.empty());
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(eq(expectedFilm),
                eq(room_1),
                eq(getExpectedTuesdayStarttime()),
                eq(getExpectedPrice(8.0))))
                .thenReturn(Optional.empty());
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(eq(expectedFilm),
                eq(room_2),
                eq(getExpectedWednesdayStarttime()),
                eq(getExpectedPrice(7.5))))
                .thenReturn(Optional.of(getExpectedWednesdayPresentation()));
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(eq(expectedFilm),
                eq(room_1),
                eq(getExpectedThursdayStarttime()),
                eq(getExpectedPrice(7.0))))
                .thenReturn(Optional.empty());
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(eq(expectedFilm),
                eq(room_3),
                eq(getExpectedFridayStarttime()),
                eq(getExpectedPrice(7.0))))
                .thenReturn(Optional.empty());
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(eq(expectedFilm),
                eq(room_1),
                eq(getExpectedSaturdayStarttime()),
                eq(getExpectedPrice(7.0))))
                .thenReturn(Optional.empty());
        when(presentationRepositoryMock.findByFilmAndRoomAndStartTimeAndPrice(eq(expectedFilm),
                eq(room_1),
                eq(getExpectedSundayStarttime()),
                eq(getExpectedPrice(7.0))))
                .thenReturn(Optional.empty());
    }

    private void verifyFilm(ArgumentCaptor<String> filmNameCaptor) {
        verify(filmServiceMock).getOrCreateFilm(filmNameCaptor.capture());
        String actualFilmName = filmNameCaptor.getValue();
        assertThat(expectedFilm.getName()).isEqualTo(actualFilmName);
    }

    private List<String> getCorrectInputPresentationLines() {
        return List.of(
                "Pumuckl;Mo/17:00/Saal 1/7.00,Di/17:00/Saal 1/8.00,Mi/14:00/Saal 2/7.50,Do/14:00/Saal 1/7.00,Fr/17:00/Saal 3/7.00,Sa/14:00/Saal 1/7.00,So/15:00/Saal 1/7.00"
        );
    }

    private Film getExpectedFilm() {
        Film film = new Film();
        film.setName("Pumuckl");
        return film;
    }

    private Room getRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        return room;
    }

    private List<Presentation> getExpectedPresentationsCompletely() {
        return List.of(
                getExpectedMondayPresentation(),
                getExpectedTuesdayPresentation(),
                getExpectedWednesdayPresentation(),
                getExpectedThursdayPresentation(),
                getExpectedFridayPresentation(),
                getExpectedSaturdayPresentation(),
                getExpectedSundayPresentation()
        );
    }

    private List<Presentation> getExpectedPresentationsWithoutWednesday() {
        return List.of(
                getExpectedMondayPresentation(),
                getExpectedTuesdayPresentation(),
                getExpectedThursdayPresentation(),
                getExpectedFridayPresentation(),
                getExpectedSaturdayPresentation(),
                getExpectedSundayPresentation()
        );
    }

    private Presentation getExpectedMondayPresentation() {
        return getExpectedPresentation(room_1,
                getExpectedMondayStarttime(),
                getExpectedPrice(7.0));
    }

    private Presentation getExpectedTuesdayPresentation() {
        return getExpectedPresentation(room_1,
                getExpectedTuesdayStarttime(),
                getExpectedPrice(8.0));
    }

    private Presentation getExpectedWednesdayPresentation() {
        return getExpectedPresentation(room_2,
                getExpectedWednesdayStarttime(),
                getExpectedPrice(7.5));
    }

    private Presentation getExpectedThursdayPresentation() {
        return getExpectedPresentation(room_1,
                getExpectedThursdayStarttime(),
                getExpectedPrice(7.0));
    }

    private Presentation getExpectedFridayPresentation() {
        return getExpectedPresentation(room_3,
                getExpectedFridayStarttime(),
                getExpectedPrice(7.0));
    }

    private Presentation getExpectedSaturdayPresentation() {
        return getExpectedPresentation(room_1,
                getExpectedSaturdayStarttime(),
                getExpectedPrice(7.0));
    }

    private Presentation getExpectedSundayPresentation() {
        return getExpectedPresentation(room_1,
                getExpectedSundayStarttime(),
                getExpectedPrice(7.0));
    }

    private Presentation getExpectedPresentation(Room room, LocalDateTime startTime, BigDecimal price) {
        Presentation presentation = new Presentation();
        presentation.setFilm(expectedFilm);
        presentation.setRoom(room);
        presentation.setStartTime(startTime);
        presentation.setPrice(price);
        return presentation;
    }

    private LocalDateTime getExpectedMondayStarttime() {
        return getNextDateTimeForCurrentWeek(DayOfWeek.MONDAY, 17);
    }

    private LocalDateTime getExpectedTuesdayStarttime() {
        return getNextDateTimeForCurrentWeek(DayOfWeek.TUESDAY, 17);
    }

    private LocalDateTime getExpectedWednesdayStarttime() {
        return getNextDateTimeForCurrentWeek(DayOfWeek.WEDNESDAY, 14);
    }

    private LocalDateTime getExpectedThursdayStarttime() {
        return getNextDateTimeForCurrentWeek(DayOfWeek.THURSDAY, 14);
    }

    private LocalDateTime getExpectedFridayStarttime() {
        return getNextDateTimeForCurrentWeek(DayOfWeek.FRIDAY, 17);
    }

    private LocalDateTime getExpectedSaturdayStarttime() {
        return getNextDateTimeForCurrentWeek(DayOfWeek.SATURDAY, 14);
    }

    private LocalDateTime getExpectedSundayStarttime() {
        return getNextDateTimeForCurrentWeek(DayOfWeek.SUNDAY, 15);
    }

    private LocalDateTime getNextDateTimeForCurrentWeek(DayOfWeek dayOfWeek, int hourOfDay) {
        return LocalDateTime
                .now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .with(TemporalAdjusters.nextOrSame(dayOfWeek))
                .with(ChronoField.CLOCK_HOUR_OF_DAY, hourOfDay)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    private BigDecimal getExpectedPrice(double value) {
        return BigDecimal
                .valueOf(value)
                .setScale(MONEY_AFTER_COMMA_DIGITS, RoundingMode.HALF_UP);
    }

}