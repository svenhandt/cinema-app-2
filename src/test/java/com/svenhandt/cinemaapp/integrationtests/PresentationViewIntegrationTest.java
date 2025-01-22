package com.svenhandt.cinemaapp.integrationtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PresentationViewIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;


    @Test
    void shouldReturnPresentationsForWeek() throws Exception {
        mockMvc.perform(get("/presentations"))
                .andExpect(status().isOk())
                .andExpect(content().json(getResourceContentAsJsonString(getFilePath("presentationsForWeek.json")))
        );
    }

    private String getResourceContentAsJsonString(String filePath) {
        Resource roomFileResource = resourceLoader.getResource(filePath);
        try {
            return roomFileResource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getFilePath(String fileName) {
        return "classpath:json/%s".formatted(fileName);
    }

}
