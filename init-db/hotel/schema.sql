CREATE TABLE hotel_booking(
   id serial PRIMARY KEY,
   client_name VARCHAR (50) NOT NULL,
   hotel_name VARCHAR (50) NOT NULL,
   arrival_date DATE NOT NULL,
   depurture_date DATE NOT NULL
);
