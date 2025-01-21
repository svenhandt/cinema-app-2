package com.svenhandt.cinemaapp.integrationtests;

import com.svenhandt.cinemaapp.persistence.entity.Film;
import com.svenhandt.cinemaapp.persistence.repository.FilmRepository;
import com.svenhandt.cinemaapp.persistence.repository.PresentationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PresentationViewIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private PresentationRepository presentationRepository;

    @Test
    void testRepositoryWork() {
        Film film = new Film();
        film.setName("Film 1");
        filmRepository.save(film);
        System.out.println(filmRepository.findByName("Film 1").get());
    }

    

}
