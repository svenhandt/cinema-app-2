package com.svenhandt.cinemaapp.filesimport;

import com.svenhandt.cinemaapp.core.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FilesImporter implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(FilesImporter.class);

    @Value("${cinemaapp.roomfiles.path}")
    private String roomFilesPath;

    private final RoomService roomService;

    public FilesImporter(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        importRoomFiles();
    }

    private void importRoomFiles() {
        try {
            Set<String> roomFileNames = getRoomFileNames();
            LOG.info("Found {} room files", roomFileNames);
            importRoomFiles(roomFileNames);
        }
        catch(IOException | URISyntaxException ex) {
            LOG.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    private void importRoomFiles(Set<String> roomFileNames) {
        roomFileNames.forEach(roomService::createRoomAndSeats);
    }

    private Set<String> getRoomFileNames() throws URISyntaxException, IOException {
        Set<String> fileNames = new HashSet<>();
        Optional<URL> fileUrlOpt = Optional.ofNullable(getClass().getClassLoader().getResource(roomFilesPath));
        if(fileUrlOpt.isPresent()) {
            URL fileUrl = fileUrlOpt.get();
            URI fileUri = fileUrl.toURI();
            fileNames.addAll(listFilesUsingFilesList(fileUri));
        }
        return fileNames;
    }

    private Set<String> listFilesUsingFilesList(URI fileUri) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(fileUri))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    private void importPresentationFiles() {

    }

}
