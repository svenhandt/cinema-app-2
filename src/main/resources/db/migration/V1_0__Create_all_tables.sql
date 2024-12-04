
CREATE TABLE film (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE room (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE seat (
    id varchar(45) NOT NULL,
    seat_row int NOT NULL,
    seat_number int NOT NULL,
    room_id int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE presentation (
    id int NOT NULL AUTO_INCREMENT,
    start_time datetime NOT NULL,
    price decimal(4, 2),
    film_id int NOT NULL,
    room_id int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (film_id) REFERENCES film(id),
    FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE booking (
    id int NOT NULL AUTO_INCREMENT,
    owner varchar(255) NOT NULL,
    payment_info varchar(45) NOT NULL,
    seat_id varchar(45) NOT NULL,
    presentation_id int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (seat_id) REFERENCES seat(id),
    FOREIGN KEY (presentation_id) REFERENCES presentation(id)
);




