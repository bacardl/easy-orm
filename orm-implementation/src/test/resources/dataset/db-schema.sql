CREATE TABLE PERSONS(ID            BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,FIRST_NAME    VARCHAR(255) NOT NULL,LAST_NAME     VARCHAR(255) NOT NULL,DATE_OF_BIRTH DATE         NOT NULL);CREATE TABLE USERS(PERSON_ID    BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,USERNAME     VARCHAR(255) UNIQUE NOT NULL,PASSWORD     VARCHAR(255)        NOT NULL,EMAIL        VARCHAR(255) UNIQUE NOT NULL,COUNTRY_CODE INTEGER);CREATE TABLE COUNTRIES(CODE INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,NAME VARCHAR(255) UNIQUE);CREATE TABLE ORDER_ITEMS(ORDER_ID   BIGINT,PRODUCT_ID BIGINT,QUANTITY   INTEGER DEFAULT 1,PRIMARY KEY (ORDER_ID, PRODUCT_ID));CREATE TABLE ORDERS(ID         BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,USER_ID    BIGINT UNIQUE NOT NULL,STATUS     VARCHAR(10) CHECK (STATUS IN ('PENDING', 'TRANSIT', 'DELIVERED')),CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP);CREATE TABLE PRODUCTS(ID          BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,NAME        VARCHAR(255) NOT NULL,PRICE       DOUBLE,DESCRIPTION VARCHAR(255));ALTER TABLE USERS ADD FOREIGN KEY (PERSON_ID) REFERENCES PERSONS (ID);ALTER TABLE USERS ADD FOREIGN KEY (COUNTRY_CODE) REFERENCES COUNTRIES (CODE);ALTER TABLE ORDER_ITEMS ADD FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ID);ALTER TABLE ORDER_ITEMS ADD FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS (ID);ALTER TABLE ORDERS ADD FOREIGN KEY (USER_ID) REFERENCES USERS (PERSON_ID);
