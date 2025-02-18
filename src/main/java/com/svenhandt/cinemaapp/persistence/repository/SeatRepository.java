package com.svenhandt.cinemaapp.persistence.repository;

import com.svenhandt.cinemaapp.persistence.entity.Room;
import com.svenhandt.cinemaapp.persistence.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, String> {

    @Query("SELECT s FROM Seat s WHERE s.room = :room ORDER BY s.seatRow, s.seatNumber")
    List<Seat> findByRoomOrderedBySeats(final Room room);

}
