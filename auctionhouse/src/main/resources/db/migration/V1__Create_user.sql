CREATE TABLE auctionuser
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(50) UNIQUE    NOT NULL,
    password     VARCHAR(50)           NOT NULL,
    first_name   VARCHAR(50)           NOT NULL,
    last_name    VARCHAR(50)           NOT NULL,
    organization VARCHAR(50)           NOT NULL,
    is_admin     BOOLEAN DEFAULT FALSE NOT NULL,
    is_blocked   BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE auction
(
    id              SERIAL PRIMARY KEY,
    symbol          VARCHAR(5) UNIQUE NOT NULL,
    quantity        BIGINT            NOT NULL,
    min_price       DOUBLE PRECISION  NOT NULL,
    close_timestamp  TIMESTAMP WITH TIME ZONE,
    status          VARCHAR(20)       NOT NULL,
    closing_summary varchar(1000),
    owner           VARCHAR(50)       NOT NULL REFERENCES auctionuser (username)
);

CREATE TABLE bid
(
    id           SERIAL PRIMARY KEY,
    quantity     BIGINT                        NOT NULL,
    price        DOUBLE PRECISION              NOT NULL,
    state        VARCHAR(20) DEFAULT 'PENDING' NOT NULL,
    win_quantity BIGINT      DEFAULT 0,
    username     VARCHAR(50)                   NOT NULL REFERENCES auctionUser (username),
    auction_id   SERIAL                        NOT NULL REFERENCES auction (id)
);


INSERT INTO auctionuser (username, password, first_name, last_name, organization, is_admin)
VALUES ('admin', 'adminpassword', 'andrew', 'ryan', 'rapture', true);