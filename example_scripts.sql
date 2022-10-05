INSERT INTO hotel ('selmify deluxe');

INSERT INTO hotel_floor (SELECT id FROM hotel WHERE hotel_name='selmify deluxe');

ALTER TABLE room
ADD CONSTRAINT room_number_within_limits CHECK (0 < room_number AND room_number < 10);