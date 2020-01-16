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

    public static final String INSERT_USER_QUERY_WITH_ID =
            "INSERT INTO users (users.id,users.login,users.password,users.email,users.country_code) " +
            "VALUES (?,?,?,?,?);";

    public static final String INSERT_USER_QUERY_WITHOUT_ID =
            "INSERT INTO users (users.login,users.password,users.email,users.country_code) " +
            "VALUES (?,?,?,?,?);";

    public static final String INSERT_COUNTRY_QUERY_WITH_CODE =
            "INSERT INTO countries (countries.code,countries.name) " +
                    "VALUES (?,?);";

    public static final String INSERT_COUNTRY_QUERY_WITHOUT_CODE =
            "INSERT INTO countries (countries.name) " +
                    "VALUES (?);";
}
