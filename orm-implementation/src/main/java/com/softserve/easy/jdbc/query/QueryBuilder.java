package com.softserve.easy.jdbc.query;

import java.util.List;

/**
 * Not implemented. For future.
 */
public interface QueryBuilder {

    String prepareSelectQuery();
    String prepareUpdateQuery(List<String> columnNames, List<Object> values);
    String prepareDeleteQuery();
    String prepareInsertQuery(List<String> columnNames, List<Object> values);
}
