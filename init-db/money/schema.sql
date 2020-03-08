CREATE SCHEMA money;

CREATE TABLE money.money(
   id serial PRIMARY KEY,
   client_name VARCHAR (50),
   amount INT NOT NULL CHECK (amount > 0)
);
