package com.softserve.orm.queryhelper;

import java.util.List;

public interface QueryBuilder {

    String prepareSelectQuery();
    String prepareUpdateQuery(List<String> columnNames, List<Object> values);
    String prepareDeleteQuery();
    String prepareInsertQuery(List<String> columnNames, List<Object> values);
}
