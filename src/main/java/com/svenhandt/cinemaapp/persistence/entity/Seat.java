package com.svenhandt.cinemaapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Seat {

    @Id
    private String id;

    private int seatRow;

    private int seatNumber;

    private String seatInfo;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "bookings_seats",
            joinColumns = @JoinColumn(name = "seat_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id")
    )
    private List<Booking> bookings;

}
