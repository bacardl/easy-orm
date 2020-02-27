package com.softserve.easy.client;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.client.entity.Country;
import com.softserve.easy.client.entity.Person;
import com.softserve.easy.client.entity.User;
import com.softserve.easy.core.Session;
import com.softserve.easy.core.SessionFactory;

import java.io.Serializable;
import java.sql.Date;

public class EasyOrmDemo {
    public static void main(String[] args) {
        System.out.println("-----------------------DEMO START!-------------------------------");
        Configuration configuration  = new Configuration();

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session currentSession = sessionFactory.getCurrentSession();

        //------------------------------------------------create--------------------------------------------------------

        Country newCountry = new Country();
        newCountry.setName("NEW_COUNTRY");
        Serializable generatedNewCountryId = currentSession.save(newCountry);
        System.out.println(generatedNewCountryId);
        System.out.println(newCountry);

        Person newPerson = new Person();
        newPerson.setId(99L);
        newPerson.setFirstName("NEW_FIRST_NAME");
        newPerson.setLastName("NEW_LAST_NAME");
        newPerson.setDateOfBirth(Date.valueOf("1999-09-09"));
        currentSession.save(newPerson);
        System.out.println(newPerson);


        User newUser = new User();
        newUser.setId(99L);
        newUser.setUsername("NEW_USERNAME");
        newUser.setPassword("NEW_PASSWORD");
        newUser.setEmail("NEW_EMAIL");
        newUser.setCountry(newCountry);
        newUser.setPerson(newPerson);
        currentSession.save(newUser);
        System.out.println(newUser);


        //------------------------------------------------create--------------------------------------------------------

        //-------------------------------------------------read---------------------------------------------------------

        User recoverableUser = currentSession.get(User.class, 1L);
        System.out.println(recoverableUser);

        Country recoverableCountry = currentSession.get(Country.class, 200);
        System.out.println(recoverableCountry);
        //------------------------------------------------/read--------------------------------------------------------

        //------------------------------------------------update--------------------------------------------------------
        recoverableUser.setEmail("UPDATED_EMAIL");
        currentSession.update(recoverableUser);
        //------------------------------------------------/update--------------------------------------------------------

        //------------------------------------------------update--------------------------------------------------------
        Country zambia = currentSession.get(Country.class, 400);
        currentSession.delete(zambia);
        //------------------------------------------------/update--------------------------------------------------------

        System.out.println("-----------------------DEMO END!-------------------------------");
    }
}
