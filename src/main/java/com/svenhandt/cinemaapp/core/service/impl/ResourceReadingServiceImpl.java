package com.svenhandt.cinemaapp.core.service.impl;

import com.svenhandt.cinemaapp.core.service.ResourceReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceReadingServiceImpl implements ResourceReadingService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceReadingServiceImpl.class);

    private final ResourceLoader resourceLoader;

    public ResourceReadingServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<String> getLinesFromFile(String filePath) {
        List<String> result = new ArrayList<>();
        Resource roomFileResource = resourceLoader.getResource(filePath);
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

}
