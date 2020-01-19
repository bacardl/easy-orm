package com.softserve.easy;

import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;

public class SimpleTestEnvironment {
    public static final Class<User> USER_CLASS = User.class;
    public static final Class<Country> COUNTRY_CLASS = Country.class;
    public static final Long USER_ID = 1L;
    public static final Integer COUNTRY_ID = 100;

    public static final User REFERENCE_USER;
    public static final Country REFERENCE_COUNTRY;

    static {
        REFERENCE_COUNTRY = new Country();
        REFERENCE_COUNTRY.setId(COUNTRY_ID);
        REFERENCE_COUNTRY.setName("United States");

        REFERENCE_USER = new User();
        REFERENCE_USER.setId(USER_ID);
        REFERENCE_USER.setUsername("Youghoss1978");
        REFERENCE_USER.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        REFERENCE_USER.setEmail("FredJPhillips@teleworm.us");
        REFERENCE_USER.setCountry(REFERENCE_COUNTRY);
    }
}
