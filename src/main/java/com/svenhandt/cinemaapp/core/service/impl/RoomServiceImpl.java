package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.RoomService;
import com.svenhandt.cinemaapp.core.service.SeatService;
import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.repository.RoomRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger LOG = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Value("${cinemaapp.roomfiles.path}")
    private String roomFilesPath;

    private final SeatService seatService;
    private final RoomRepository roomRepository;
    private final ResourceLoader resourceLoader;

    public RoomServiceImpl(SeatService seatService, RoomRepository roomRepository, ResourceLoader resourceLoader) {
        this.seatService = seatService;
        this.roomRepository = roomRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void createRoomAndSeats(String roomFileName) {
        String roomName = getRoomName(roomFileName);
        if(roomAlreadyExists(roomName)) {
            LOG.info("Room with name {} already exists, so nothing to import", roomName);
        }
        else {
            List<String> seatArrangementLines = getSeatArrangementLines(roomFileName);
            createRoomAndSeats(roomFileName, seatArrangementLines);
        }
    }

    private boolean roomAlreadyExists(String roomName) {
        Optional<Room> room = roomRepository.findByName(roomName);
        return room.isPresent();
    }

    private List<String> getSeatArrangementLines(String roomFileName) {
        String roomFilePath = "classpath:%s/%s".formatted(roomFilesPath, roomFileName);
        Resource roomFileResource = resourceLoader.getResource(roomFilePath);
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(roomFileResource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return result;
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
