package com.softserve.easy.meta.primarykey;

import com.softserve.easy.constant.PrimaryKeyType;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.InternalMetaField;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SinglePrimaryKey extends AbstractMetaPrimaryKey {
    private final InternalMetaField primaryKey;

    public SinglePrimaryKey(InternalMetaField primaryKey, MetaData entityMetaData, Field field, boolean isGeneratedPk) {
        super(entityMetaData, PrimaryKeyType.SINGLE, field, isGeneratedPk);
        this.primaryKey = primaryKey;
    }


    @Override
    public Serializable parseIdValue(ResultSet resultSet) throws SQLException {
        return (Serializable) resultSet.getObject(primaryKey.getDbFieldFullName());
    }

    @Override
    public int getNumberOfPrimaryKeys() {
        return 1;
    }

    public InternalMetaField getPrimaryKey() {
        return primaryKey;
    }

}
