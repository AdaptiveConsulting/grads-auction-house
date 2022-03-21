CREATE TABLE auction_lot
(
    id                  SERIAL PRIMARY KEY,
    owner_id            SERIAL      NOT NULL,
    symbol              VARCHAR(50) NOT NULL,
    min_price           NUMERIC     NOT NULL,
    quantity            INTEGER     NOT NULL,
    status              VARCHAR(10) DEFAULT 'OPENED',
    create_time         TIMESTAMP   DEFAULT now(),
    total_sold_quantity INTEGER     DEFAULT NULL,
    total_revenue       NUMERIC     DEFAULT NULL,
    closing_time        TIMESTAMP   DEFAULT NULL
);

CREATE TABLE auction_bid
(
    id           SERIAL PRIMARY KEY,
    auction_id   SERIAL      NOT NULL,
    user_id      SERIAL      NOT NULL,
    quantity     INTEGER     NOT NULL,
    price        NUMERIC     NOT NULL,
    state        VARCHAR(10) NOT NULL,
    win_quantity INTEGER DEFAULT 0
);
