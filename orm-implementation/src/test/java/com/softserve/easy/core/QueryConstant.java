package com.softserve.easy.core;

public class QueryConstant {
    public static final String SELECT_USERS_QUERY_WITHOUT_SCHEMA_NAME =
            "SELECT users.id,users.username,users.password,users.email,countries.code,countries.name " +
                    "FROM users " +
                    "LEFT JOIN countries " +
                    "ON users.country_code = countries.code;";

    public static final String SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME =
            "SELECT users.id,users.username,users.password,users.email,countries.code,countries.name " +
                    "FROM users " +
                    "LEFT JOIN countries " +
                    "ON users.country_code = countries.code " +
                    "WHERE users.id = ?;";

    public static final String SELECT_COUNTRY_QUERY_WITHOUT_SCHEMA_NAME =
            "SELECT countries.code,countries.name " +
                    "FROM countries;";

    public static final String SELECT_COUNTRY_BY_ID_WITHOUT_SCHEMA_NAME =
            "SELECT countries.code,countries.name " +
                    "FROM countries " +
                    "WHERE countries.code = ?;";

    public static final  String DELETE_USER_QUERY_WHERE_PRIMARYKEY =
            "DELETE FROM users WHERE users.id = ?";

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
