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
}
