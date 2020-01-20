package com.softserve.easy;

public class QueryConstant {
    public static final String SELECT_USERS_QUERY =
            "SELECT users.id,users.username,users.password,users.email,countries.code,countries.name " +
                    "FROM users " +
                    "LEFT JOIN countries " +
                    "ON users.country_code = countries.code;";

    public static final String SELECT_USER_BY_ID_1 =
            "SELECT t0.id AS users.id,t0.username AS users.username," +
                    "t0.password AS users.password,t0.email AS users.email,t0.country_code AS users.country_code," +
                    "t1.code AS countries.code,t1.name AS countries.name " +
                    "FROM public.users t0 " +
                    "INNER JOIN public.countries t1 ON (t0.country_code = t1.code) " +
                    "WHERE (t0.id = 1)";

    public static final String SELECT_COUNTRY_QUERY =
            "SELECT countries.code,countries.name " +
                    "FROM countries;";

    public static final String SELECT_COUNTRY_BY_ID =
            "SELECT t1.code AS countries.code,t1.name AS countries.name " +
                    "FROM public.countries t1 " +
                    "WHERE (t1.code = 100)";

    public static final  String DELETE_USER_QUERY_WHERE_PRIMARYKEY =
            "DELETE FROM public.users WHERE (id = 3)";

    public static final String UPDATE_USER_QUERY_WHERE_PRIMARYKEY =
            "UPDATE public.users SET country_code = 100,username = 'Wittiould1980'," +
                    "password = '$2y$10$pRmUEPJMB0/CR5uCuqCA2ODdE0iOJswpXFdIIhZmuZyiZIMe.OCl2'," +
                    "email = 'VOT_ETO_DA@armyspy.com' " +
                    "WHERE (id = 3)";

    public static final String INSERT_USER_QUERY_WITH_ID =
            "INSERT INTO public.users (country_code,username,password,email,id) " +
                    "VALUES (100,'jon','jon111','jon@gmail.com',404)";

    public static final String INSERT_USER_QUERY_WITHOUT_ID =
            "INSERT INTO public.users (country_code,username,password,email) " +
                    "VALUES (100,'jon','jon111','jon@gmail.com')";

    public static final String INSERT_COUNTRY_QUERY_WITH_CODE =
            "INSERT INTO public.countries (name,code) VALUES ('Japan',900)";

    public static final String INSERT_COUNTRY_QUERY_WITHOUT_CODE =
            "INSERT INTO public.countries (name) VALUES ('Japan')";
}
