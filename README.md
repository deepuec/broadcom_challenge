# Broadcom
Interview Challenge

This repo contains docker compose file to bring up ui and backend service.
Folllow commands to run the application

## Prerequisites
docker runtime

## Setting up the application
- Clone repository git clone https://github.com/deepuec/broadcom_challenge.git
-  `cd broadcom_challenge`
-  `docker-compose build`
-  `docker-compose up`
-  Make sure all 3 containers are up `docker ps`
-  Connect to DB and make sure `USERS` table is created

    Server : localhost:5432 \
    username: postgres \
    password: postgres \
    db: postgres 
-  To load user data, execute `scripts\insert_users_100.sql` . This will load 100 users to table
-  To load million users, follow [Load Million Users](#load-million-users)
-  Open UI app in browser http://localhost:9090
- API URL http://localhost:8080/api/users


## Load Million Users
Million test users are loaded in `scripts\user_data.csv` file. To load these data into table, follow below steps
- Truncate table, `truncate users`
- Copy csv file into `postgres-data` folder created as part of the docker-compose up in the working directory
- Get postgres containser name `docker ps`
- `docker exec -it [CONTAINER_NAME] psql -U postgres -d postgres` 
- `COPY users(id,first_name,last_name,age,address_1,address_2) FROM '/var/lib/postgresql/data/user_data.csv' DELIMITER ','  CSV HEADER;`
- Wait for sometime and then creat below 2 indexes which will help improve sql performance for the filtering

`CREATE INDEX idx_users_name_age ON users (last_name desc, age desc);` 

`CREATE INDEX idx_users_name_age ON users (age desc);`
- Give some time for DB to warm up and test the apis