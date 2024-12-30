CREATE TABLE if not exists fiat_rates (
                                     id SERIAL PRIMARY KEY,
                                     currency VARCHAR(10) NOT NULL,
                                     rate NUMERIC(30,  15) NOT NULL,
                                     timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE if not exists crypto_rates (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(10) NOT NULL,
                                       value DECIMAL(30, 15) NOT NULL,
                                       timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
