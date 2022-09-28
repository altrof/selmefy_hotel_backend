CREATE TABLE hotel(
    id SERIAL PRIMARY KEY,
    hotel_name VARCHAR(100)
);

CREATE TABLE hotel_floor(
    id BIGSERIAL PRIMARY KEY,
    hotel_id SMALLINT,
    FOREIGN KEY (hotel_id) REFERENCES hotel (id) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE room(
    id BIGSERIAL PRIMARY KEY,
    size FLOAT,
    number_of_beds SMALLINT,
    floor_id INT,
    room_number SMALLINT,
    FOREIGN KEY (floor_id) REFERENCES hotel_floor (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT unique_room_number_for_floor UNIQUE (floor_id, room_number),
    CONSTRAINT room_number_within_limits CHECK (0 < room_number AND room_number < 10)
    -- if we have the time, we can replace the hardcoded CONSTRAINT room_number_within_limits 
    -- with a dynamic value that references to some capacity value
    -- in the hotel_floor table. Has to use triggers.
);


CREATE TABLE person(
    id BIGSERIAL PRIMARY KEY,
    identity_code VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    time_of_registration TIMESTAMP DEFAULT LOCALTIMESTAMP(0),
    CONSTRAINT first_name_not_empty CHECK (REGEXP_REPLACE(first_name, '\s+$', '') <> ''),
	CONSTRAINT last_name_not_empty CHECK (REGEXP_REPLACE(last_name, '\s+$', '') <> ''),
    CONSTRAINT date_of_birth_less_than_registration CHECK (date_of_birth < DATE(time_of_registration))
);

CREATE TABLE booking (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT,
    person_id VARCHAR(255),
    price SMALLINT,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    comments VARCHAR(1000),
    late_check_out BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (person_id) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT check_in_not_after_check_out CHECK (check_in_date < check_out_date),
    CONSTRAINT check_in_not_in_past CHECK (check_in_date >= DATE(LOCALTIMESTAMP(0)))
);

CREATE TABLE user_account (
    id BIGSERIAL PRIMARY KEY,
    person_id VARCHAR(255),
    e_mail VARCHAR(255) UNIQUE,
    account_user_name VARCHAR(255) UNIQUE,
    password_hash TEXT, -- Needs further research
    is_client BOOLEAN NOT NULL DEFAULT FALSE, -- Alternatively additional table for account types
    is_employee BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (person_id) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT client_and_employee_cannot_both_be_false CHECK 
        (is_client IS TRUE OR is_employee IS TRUE)
);

CREATE TABLE employee_status (
    id SMALLINT PRIMARY KEY,
    status_name VARCHAR(100)
);

CREATE TABLE employee_role (
    id SMALLSERIAL PRIMARY KEY,
    role_name VARCHAR(255) 
);

CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    person_id VARCHAR(255),
    employee_start_date DATE NOT NULL,
    employee_end_date DATE NOT NULL,
    employee_role SMALLINT,
    employee_status SMALLINT,
    FOREIGN KEY (person_id) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (employee_role) REFERENCES employee_role (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (employee_status) REFERENCES employee_status (id) ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE hotel_service (
    id SMALLSERIAL PRIMARY KEY,
    hotel_service_name VARCHAR(255)
);

CREATE TABLE hotel_service_order (
    id BIGSERIAL PRIMARY KEY,
    service_type SMALLINT,
    person_id VARCHAR(255),
    order_time TIMESTAMP DEFAULT LOCALTIMESTAMP(0),
    price SMALLINT,
    comments VARCHAR(1000),
    FOREIGN KEY (service_type) REFERENCES hotel_service (id) ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (person_id) REFERENCES person (identity_code) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT order_time_not_in_past CHECK (order_time >= DATE(LOCALTIMESTAMP(0)))
);

