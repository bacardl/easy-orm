package com.softserve.easy.sql;

import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UpdateQuery;

import java.io.Serializable;

public interface SqlManager {
    SelectQuery buildSelectByIdSqlQuery(Class<?> entity, Serializable id);

    SelectQuery buildSelectAllSqlQuery(Class<?> entity);

    UpdateQuery buildUpdateQuery(Class<?> entity);

    DeleteQuery buildDeleteQuery(Class<?> entity);

    InsertQuery buildInsertQueryWithId(Class<?> entity, Serializable id);

    InsertQuery buildInsertQuery(Class<?> entity);
}
