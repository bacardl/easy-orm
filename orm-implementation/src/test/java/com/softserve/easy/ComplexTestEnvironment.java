package com.softserve.easy;

import com.softserve.easy.entity.complex.*;

import java.sql.Date;

public class ComplexTestEnvironment {
    public static final Class<User> USER_CLASS = User.class;
    public static final Class<Country> COUNTRY_CLASS = Country.class;
    public static final Class<Person> PERSON_CLASS = Person.class;
    public static final Class<Order> ORDER_CLASS = Order.class;
    public static final Class<Product> PRODUCT_CLASS = Product.class;
    public static final Class<OrderProductId> ORDER_PRODUCT_ID_CLASS = OrderProductId.class;
    public static final Class<OrderProduct> ORDER_PRODUCT_CLASS = OrderProduct.class;

    public static final Long USER_ID = 1L;
    public static final Long PERSON_ID = 1L;
    public static final Integer COUNTRY_ID = 100;
    public static final Long ORDER_ID = 1L;
    public static final Long PRODUCT_ID = 2L;
    public static final User REFERENCE_USER;
    public static final Country REFERENCE_COUNTRY;
    public static final Person REFERENCE_PERSON;
    public static final Order REFERENCE_ORDER;
    public static final Product REFERENCE_PRODUCT;
    public static final OrderProduct REFERENCE_ORDER_PRODUCT;
    public static final OrderProductId REFERENCE_ORDER_PRODUCT_ID;

    static {
        REFERENCE_COUNTRY = new Country();
        REFERENCE_COUNTRY.setId(COUNTRY_ID);
        REFERENCE_COUNTRY.setName("United States");

        REFERENCE_PERSON = new Person();
        REFERENCE_PERSON.setId(PERSON_ID);
        REFERENCE_PERSON.setFirstName("Fred");
        REFERENCE_PERSON.setLastName("Phillips");
        REFERENCE_PERSON.setDateOfBirth(Date.valueOf("1990-01-20"));


        REFERENCE_USER = new User();
        REFERENCE_USER.setId(USER_ID);
        REFERENCE_USER.setUsername("Youghoss1978");
        REFERENCE_USER.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        REFERENCE_USER.setEmail("FredJPhillips@teleworm.us");
        REFERENCE_USER.setPerson(REFERENCE_PERSON);
        REFERENCE_USER.setCountry(REFERENCE_COUNTRY);

        REFERENCE_PRODUCT = new Product();
        REFERENCE_PRODUCT.setId(PRODUCT_ID);
        REFERENCE_PRODUCT.setName("Apple MacBook Pro MGXA2LL/A");
        REFERENCE_PRODUCT.setPrice(830.00d);
        REFERENCE_PRODUCT.setDescription("15-Inch Laptop with Retina Display (2.2 GHz Intel Core i7 Processor, 16GB RAM, 256GB SSD) (Renewed)");

        REFERENCE_ORDER = new Order();
        REFERENCE_ORDER.setId(ORDER_ID);
        REFERENCE_ORDER.setUser(REFERENCE_USER);
        REFERENCE_ORDER.setStatus("DELIVERED");
        REFERENCE_ORDER.setCreatedAt(Date.valueOf("2019-09-20"));

        REFERENCE_ORDER_PRODUCT_ID = new OrderProductId(ORDER_ID, PRODUCT_ID);

        REFERENCE_ORDER_PRODUCT = new OrderProduct(REFERENCE_ORDER, REFERENCE_PRODUCT, 2);
    }

}
