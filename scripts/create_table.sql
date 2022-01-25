
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
  id SERIAL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  age int NOT NULL,
  address_1 varchar(150)NOT NULL,
  address_2 varchar(150),
  PRIMARY KEY (id)
);

drop index if exists idx_users_name_age;

CREATE INDEX idx_users_name_age ON users (last_name desc, age desc);