CREATE TABLE auction_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR (50) UNIQUE NOT NULL,
    password VARCHAR (50) NOT NULL,
    is_admin BOOLEAN NOT NULL,
    first_name VARCHAR (50) NOT NULL,
    lastName VARCHAR (50) NOT NULL,
    organisation VARCHAR (50) NOT NULL,
    is_blocked BOOLEAN NOT NUll,
);