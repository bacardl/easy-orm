CREATE TABLE PERSONS(ID            BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,FIRST_NAME    VARCHAR(255) NOT NULL,LAST_NAME     VARCHAR(255) NOT NULL,DATE_OF_BIRTH DATE         NOT NULL);CREATE TABLE USERS(PERSON_ID    BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,USERNAME     VARCHAR(255) UNIQUE NOT NULL,PASSWORD     VARCHAR(255)        NOT NULL,EMAIL        VARCHAR(255) UNIQUE NOT NULL,COUNTRY_CODE INTEGER);CREATE TABLE COUNTRIES(CODE INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,NAME VARCHAR(255) UNIQUE NOT NULL);CREATE TABLE ORDER_ITEMS(ORDER_ID   BIGINT NOT NULL,PRODUCT_ID BIGINT NOT NULL,QUANTITY   INTEGER DEFAULT 1,PRIMARY KEY (ORDER_ID, PRODUCT_ID));CREATE TABLE ORDERS(ID         BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,USER_ID    BIGINT,STATUS     VARCHAR(10) CHECK (STATUS IN ('PENDING', 'TRANSIT', 'DELIVERED')),CREATED_AT DATE DEFAULT CURRENT_DATE);CREATE TABLE PRODUCTS(ID          BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,NAME        VARCHAR(255) NOT NULL,PRICE       DOUBLE,DESCRIPTION VARCHAR(255));ALTER TABLE USERS ADD CONSTRAINT USER_PERSON_FK FOREIGN KEY (PERSON_ID) REFERENCES PERSONS (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;ALTER TABLE USERS ADD CONSTRAINT USER_COUNTRY_FK FOREIGN KEY (COUNTRY_CODE) REFERENCES COUNTRIES (CODE) ON UPDATE NO ACTION ON DELETE SET NULL;ALTER TABLE ORDER_ITEMS ADD CONSTRAINT ORDER_ITEMS_ORDER_FK FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ID) ON UPDATE NO ACTION ON DELETE NO ACTION ;ALTER TABLE ORDER_ITEMS ADD CONSTRAINT ORDER_ITEMS_PRODUCT_FK FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS (ID) ON UPDATE NO ACTION ON DELETE NO ACTION ;ALTER TABLE ORDERS ADD CONSTRAINT USER_ORDER_FK FOREIGN KEY (USER_ID) REFERENCES USERS (PERSON_ID) ON UPDATE NO ACTION ON DELETE SET NULL;
