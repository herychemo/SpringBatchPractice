
DROP TABLE IF EXISTS people;
CREATE TABLE people (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    email varchar(80) NOT NULL
);

