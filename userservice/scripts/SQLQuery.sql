TRUNCATE TABLE users; 
drop index if exists idx_users_name_age;


COPY users
FROM 'user_data.csv' 
DELIMITER ',' 
CSV HEADER;


CREATE INDEX idx_users_name_age ON users (lower(last_name) desc, age desc);