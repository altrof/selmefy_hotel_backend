CREATE TABLE person(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE NOT NULL,
    time_of_registration TIMESTAMP DEFAULT LOCALTIMESTAMP(0),
    CONSTRAINT first_name_not_empty CHECK (REGEXP_REPLACE(first_name, '\s+$', '') <> ''),
	CONSTRAINT last_name_not_empty CHECK (REGEXP_REPLACE(last_name, '\s+$', '') <> ''),
    CONSTRAINT date_of_birth_less_than_registration CHECK (date_of_birth DATE NOT NULL,
        < DATE(time_of_registration)),
	CONSTRAINT first_or_last_name_not_null CHECK (
		(first_name IS NOT NULL AND last_name IS NOT NULL)
		OR (first_name IS NULL AND last_name IS NOT NULL)
		OR (first_name IS NOT NULL AND last_name IS NULL))
	);
);
