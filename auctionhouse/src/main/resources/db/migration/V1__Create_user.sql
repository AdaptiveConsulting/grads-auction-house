CREATE TABLE auction_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR (50) UNIQUE NOT NULL,
    password VARCHAR (50) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    first_name VARCHAR (50) NOT NULL,
    last_name VARCHAR (50) NOT NULL,
    organisation VARCHAR (50) NOT NULL,
    blocked BOOLEAN NOT NUll DEFAULT FALSE
);

INSERT INTO auction_user (
    username, password, is_admin, first_name, last_name, organisation, blocked
) VALUES ('ADMIN', 'adminpassword', TRUE, 'admin', 'admin', 'Adaptive', FALSE);