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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PresentationServiceImplTest {

    @Mock
    private FilmService filmServiceMock;

    @Mock
    private ResourceReadingService resourceReadingServiceMock;

    @Mock
    private RoomService roomServiceMock;

    @Mock
    private PresentationRepository presentationRepositoryMock;

    private final List<String> inputPresentationLines = getInputPresentationLines();
    private final Film expectedFilm = getExpectedFilm();
    private final Room room_1 = getRoom("Saal 1");
    private final Room room_2 = getRoom("Saal 2");
    private final Room room_3 = getRoom("Saal 3");


    private PresentationServiceImpl presentationServiceImpl;

    @BeforeEach
    void setup() {
        String presentationFilePath = "presentations/presentations.txt";
        presentationServiceImpl = new PresentationServiceImpl(presentationFilePath,
                filmServiceMock,
                resourceReadingServiceMock,
                roomServiceMock,
                presentationRepositoryMock);
    }

    @Test
    void shouldSuccessfullyInitPresentations() {

    }

    private List<String> getInputPresentationLines() {
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

    private List<Presentation> getExpectedPresentations() {
        return List.of(
                new
        )
    }

    private Presentation getExpectedMondayPresentation() {
        Presentation presentation = new Presentation();
        return presentation;
    }

}