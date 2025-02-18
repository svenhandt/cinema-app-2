package com.svenhandt.cinemaapp.integrationtests;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestUtil {

    static String getResourceContentAsJsonString(ResourceLoader resourceLoader, String filePath) {
        Resource roomFileResource = resourceLoader.getResource(filePath);
        try {
            return roomFileResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String getFilePath(String fileName) {
        return "classpath:json/%s".formatted(fileName);
    }
}
