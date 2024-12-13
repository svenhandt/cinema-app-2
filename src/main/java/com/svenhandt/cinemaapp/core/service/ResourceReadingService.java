package com.svenhandt.cinemaapp.core.service;

import java.util.List;

public interface ResourceReadingService {
    List<String> getLinesFromFile(String filePath);
}
