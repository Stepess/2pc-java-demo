CREATE TABLE fly_booking(
   id serial PRIMARY KEY,
   client_name VARCHAR (50) NOT NULL,
   fly_number VARCHAR (50) UNIQUE NOT NULL,
   arrival_place VARCHAR (50) NOT NULL,
   destination_place VARCHAR (50) NOT NULL,
   arrival_date DATE NOT NULL
);
