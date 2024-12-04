package com.svenhandt.cinemaapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Presentation {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    private int id;

    private LocalDateTime startTime;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="film_id")
    private Film film;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

}
