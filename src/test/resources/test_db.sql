CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    email VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL
);

INSERT INTO client (email, password)
VALUES ('test1@mail.ru', 'test1test1'),
       ('test2@mail.ru', 'test2test2');