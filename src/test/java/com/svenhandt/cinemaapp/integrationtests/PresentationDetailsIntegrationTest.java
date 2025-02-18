package com.svenhandt.cinemaapp.integrationtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PresentationDetailsIntegrationTest {

    private static final int ID_PRESENTATION_FRIDAY = 5;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void shouldReturnDetailsForFridayPresentation() throws Exception {
        String expectedContentAsJsonString = TestUtil.getResourceContentAsJsonString(resourceLoader, TestUtil.getFilePath("detailsForFridayPresentation.json"));
        mockMvc.perform(get("/presentations/%d".formatted(ID_PRESENTATION_FRIDAY)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedContentAsJsonString)
                );
    }

}
