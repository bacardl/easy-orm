package com.softserve.orm.queryhelper;

public interface QueryBuilder {

    String prepareSelectQuery();
    String prepareUpdateQuery();
    String prepareDeleteQuery();
    QueryBuilder where();
    QueryBuilder like(LikeType likeType, Object requiredObj, String columnName);
    QueryBuilder and();
    QueryBuilder or();
    QueryBuilder eqls(String columnName, Object requiredOdj);
}
