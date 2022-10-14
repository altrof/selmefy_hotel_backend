CREATE TABLE hotel(
    id SERIAL PRIMARY KEY NOT NULL,
    hotel_name VARCHAR(100) NOT NULL
);

CREATE TABLE hotel_floor(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    hotel_id SMALLINT NOT NULL,
    CONSTRAINT fk_hotel_id FOREIGN KEY (hotel_id) REFERENCES hotel (id) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE room(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    size FLOAT NOT NULL,
    number_of_beds SMALLINT NOT NULL,
    floor_id INT NOT NULL,
    room_number SMALLINT NOT NULL,
    CONSTRAINT fk_floor_id FOREIGN KEY (floor_id) REFERENCES hotel_floor (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT unique_room_number_for_floor UNIQUE (floor_id, room_number),
    CONSTRAINT room_number_within_limits CHECK (0 < room_number AND room_number < 10)
    -- if we have the time, we can replace the hardcoded CONSTRAINT room_number_within_limits 
    -- with a dynamic value that references to some capacity value
    -- in the hotel_floor table. Has to use triggers.
);

CREATE TABLE person(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    identity_code VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    time_of_registration TIMESTAMP DEFAULT LOCALTIMESTAMP(0) NOT NULL,
    CONSTRAINT first_name_not_empty CHECK (REGEXP_REPLACE(first_name, '\s+$', '') <> ''),
	CONSTRAINT last_name_not_empty CHECK (REGEXP_REPLACE(last_name, '\s+$', '') <> ''),
    CONSTRAINT date_of_birth_less_than_registration CHECK (date_of_birth < DATE(time_of_registration))
);

CREATE TABLE booking (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    person_identity_code VARCHAR(255) NOT NULL,
    price SMALLINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    comments VARCHAR(1000),
    late_check_out BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_room_id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_id FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_identity_code FOREIGN KEY (person_identity_code) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT check_in_not_after_check_out CHECK (check_in_date < check_out_date),
    CONSTRAINT check_in_not_in_past CHECK (check_in_date >= DATE(LOCALTIMESTAMP(0)))
);

CREATE TABLE user_account (
    person_id BIGINT PRIMARY KEY,
    person_identity_code VARCHAR(255) NOT NULL,
    e_mail VARCHAR(255) UNIQUE NOT NULL,
    account_user_name VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL, -- Needs further research
    is_client BOOLEAN NOT NULL DEFAULT FALSE, -- Alternatively additional table for account types
    is_employee BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_person_id FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_identity_code FOREIGN KEY (person_identity_code) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT client_and_employee_cannot_both_be_false CHECK 
        (is_client IS TRUE OR is_employee IS TRUE)
);

CREATE TABLE employee_status (
    id SMALLINT PRIMARY KEY,
    status_name VARCHAR(100) NOT NULL
);

CREATE TABLE employee_role (
    id SMALLSERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL
);

CREATE TABLE employee (
    person_id BIGINT PRIMARY KEY NOT NULL,
    person_identity_code VARCHAR(255) NOT NULL,
    employee_start_date DATE NOT NULL,
    employee_end_date DATE NOT NULL,
    employee_role SMALLINT NOT NULL,
    employee_status SMALLINT NOT NULL,
    CONSTRAINT fk_person_id FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_identity_code FOREIGN KEY (person_identity_code) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_employee_role FOREIGN KEY (employee_role) REFERENCES employee_role (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_employee_status FOREIGN KEY (employee_status) REFERENCES employee_status (id) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE hotel_service (
    id SMALLSERIAL PRIMARY KEY,
    hotel_service_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE hotel_service_order (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    service_type SMALLINT NOT NULL,
    person_id VARCHAR(255) NOT NULL,
    order_time TIMESTAMP DEFAULT LOCALTIMESTAMP(0) NOT NULL,
    price SMALLINT NOT NULL,
    comments VARCHAR(1000),
    CONSTRAINT fk_service_type FOREIGN KEY (service_type) REFERENCES hotel_service (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_id FOREIGN KEY (person_id) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT order_time_not_in_past CHECK (order_time >= DATE(LOCALTIMESTAMP(0)))
);

CREATE TABLE person_in_booking(
    booking_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    person_identity_code VARCHAR(255) NOT NULL,
    CONSTRAINT pk_person_in_booking PRIMARY KEY (booking_id, person_id),
    CONSTRAINT fk_booking_id FOREIGN KEY (booking_id) REFERENCES booking (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_id FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_identity_code FOREIGN KEY (person_identity_code) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE
    );

CREATE TABLE person_in_service_order(
    order_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    person_identity_code VARCHAR(255) NOT NULL,
    CONSTRAINT pk_person_in_service_order PRIMARY KEY (order_id, person_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES hotel_service_order (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_id FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT fk_person_identity_code FOREIGN KEY (person_identity_code) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE
    );
