-- !Ups

CREATE TABLE IF NOT EXISTS "user"
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS role
(
    id   SERIAL PRIMARY KEY,
    role VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,

    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES role (id)
);


INSERT INTO "user"(username, password)
VALUES ('test_user_1', 'password1'),
       ('test_user_2', 'password2');

INSERT INTO role(role)
VALUES ('ADMIN');

INSERT INTO user_role(user_id, role_id)
VALUES (1, 1);


-- !Downs

DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;
