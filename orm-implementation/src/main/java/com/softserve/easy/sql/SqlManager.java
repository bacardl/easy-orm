package com.softserve.easy.sql;

import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UpdateQuery;
import com.softserve.easy.meta.MetaData;

import java.io.Serializable;

public interface SqlManager {
    SelectQuery buildSelectByPkQuery(MetaData entityMetaData, Serializable id);

    SelectQuery buildSelectAllQuery(MetaData entityMetaData);

    UpdateQuery buildUpdateByPkQuery(MetaData entityMetaData, Object object);

    DeleteQuery buildDeleteByPkQuery(MetaData entityMetaData, Object object);

    InsertQuery buildInsertQueryWithId(MetaData entityMetaData, Object object, Serializable id);

    InsertQuery buildInsertQuery(MetaData entityMetaData, Object object);
}
