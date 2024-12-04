
CREATE TABLE BookingsSeats (
    booking_id int,
    seat_id varchar(45),
    FOREIGN KEY (booking_id) REFERENCES booking(id),
    FOREIGN KEY (seat_id) REFERENCES seat(id)
);




