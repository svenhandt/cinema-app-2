package com.svenhandt.cinemaapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    private int id;

    private String owner;

    private String paymentInfo;

    @ManyToOne
    @JoinColumn(name="presentation_id")
    private Presentation presentation;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "bookings_seats",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<Seat> seats;

}
