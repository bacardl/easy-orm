CREATE TABLE USERS(ID           BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,USERNAME     VARCHAR(255) UNIQUE NOT NULL,PASSWORD     VARCHAR(255)        NOT NULL,EMAIL        VARCHAR(255) UNIQUE NOT NULL,COUNTRY_CODE INTEGER);CREATE TABLE COUNTRIES(CODE INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,NAME VARCHAR(255) UNIQUE NOT NULL);ALTER TABLE USERS ADD FOREIGN KEY (COUNTRY_CODE) REFERENCES COUNTRIES (CODE);
