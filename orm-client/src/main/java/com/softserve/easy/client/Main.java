package com.softserve.easy.client;

import com.softserve.easy.client.dao.UserDao;
import com.softserve.easy.client.dao.UserDaoImpl;
import com.softserve.easy.client.entity.Country;
import com.softserve.easy.client.entity.User;

public class Main {
    public static void main(String[] args) {
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(1L);
        user.setUsername("Youghoss1978");
        user.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        user.setEmail("FredJPhillips@teleworm.us");
        user.setCountry(country);

        //UserDao userDao = new UserDaoImpl();
        //userDao.get(1L);
    }
}
