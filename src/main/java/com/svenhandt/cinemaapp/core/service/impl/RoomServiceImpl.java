package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.ResourceReadingService;
import com.svenhandt.cinemaapp.core.service.RoomService;
import com.svenhandt.cinemaapp.core.service.SeatService;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger LOG = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final String roomFilesPath;

    private final SeatService seatService;
    private final RoomRepository roomRepository;
    private final ResourceReadingService resourceReadingService;

    public RoomServiceImpl(@Value("${cinemaapp.roomfiles.path}") String roomFilesPath,
                           SeatService seatService,
                           RoomRepository roomRepository,
                           ResourceReadingService resourceReadingService) {
        this.roomFilesPath = roomFilesPath;
        this.seatService = seatService;
        this.roomRepository = roomRepository;
        this.resourceReadingService = resourceReadingService;
    }

    @Override
    @Transactional
    public void createRoomAndSeats(String roomFileName) {
        Validate.notEmpty(roomFileName);
        String roomName = getRoomName(roomFileName);
        if(roomAlreadyExists(roomName)) {
            LOG.info("Room with name {} already exists, so nothing to import", roomName);
        }
        else {
            List<String> seatArrangementLines = getSeatArrangementLines(roomFileName);
            createRoomAndSeats(roomFileName, seatArrangementLines);
        }
    }

    @Override
    public Room getRoom(String roomName) {
        return roomRepository.findByName(roomName).orElseThrow(() -> new EntityNotFoundException("Room with name %s not found".formatted(roomName)));
    }

    private boolean roomAlreadyExists(String roomName) {
        Optional<Room> room = roomRepository.findByName(roomName);
        return room.isPresent();
    }

    private List<String> getSeatArrangementLines(String roomFileName) {
        String roomFilePath = "classpath:%s/%s".formatted(roomFilesPath, roomFileName);
        return resourceReadingService.getLinesFromFile(roomFilePath);
    }

    private void createRoomAndSeats(String roomFileName, List<String> seatArrangementLines) {
        Room room = createRoom(roomFileName);
        seatService.createSeats(room, seatArrangementLines);
    }

    private Room createRoom(String roomFileName) {
        String roomName = getRoomName(roomFileName);
        Room room = new Room();
        room.setName(roomName);
        LOG.info("create room {}", roomName);
        roomRepository.save(room);
        return room;
    }

    private String getRoomName(String roomFileName) {
        String roomNameLowerCase = StringUtils.replaceEach(roomFileName,
                ArrayUtils.toArray(".txt", "_"),
                ArrayUtils.toArray(StringUtils.EMPTY, " "));
        return StringUtils.capitalize(
                roomNameLowerCase);
    }

}
