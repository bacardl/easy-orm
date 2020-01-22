package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.softserve.easy.meta.MetaData;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InternalMetaField extends AbstractMetaField {
    private final String dbFieldName;
    private final boolean isPrimaryKey;
    private final DbColumn internalDbColumn;

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public DbColumn getInternalDbColumn() {
        return internalDbColumn;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public String getDbFieldFullName() {
        return metaData.getEntityDbName() + "." + dbFieldName;
    }

    @Override
    public Serializable retrieveValue(Object object) throws IllegalAccessException {
        boolean previous = this.field.isAccessible();
        this.field.setAccessible(true);
        Serializable value = (Serializable) this.field.get(object);
        this.field.setAccessible(previous);
        return value;
    }

    public Object parseValue(ResultSet resultSet) throws SQLException {
        return resultSet.getObject(getDbFieldFullName());
    }

    protected static abstract class Init<T extends Init<T>> extends AbstractMetaField.Init<T> {
        private String dbFieldName;
        private boolean isPrimaryKey;

        public T dbFieldName(String dbFieldName) {
            this.dbFieldName = dbFieldName;
            return self();
        }

        public T setPrimaryKey(boolean flag) {
            this.isPrimaryKey = flag;
            return self();
        }

        public InternalMetaField build() {
            return new InternalMetaField(this);
        }
    }

    public static class Builder extends Init<Builder> {
        public Builder(Field field, MetaData metaData) {
            this.field = field;
            this.metaData = metaData;
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    protected InternalMetaField(Init<?> init) {
        super(init);
        this.dbFieldName = init.dbFieldName;
        this.isPrimaryKey = init.isPrimaryKey;
        this.internalDbColumn = getMetaData().getDbTable()
                .addColumn(dbFieldName, getMappingType().getSqlType(), null);
        if (isPrimaryKey) {
            getMetaData()
                    .getDbTable()
                    .primaryKey(getMetaData().getEntityDbName() + "_PK", dbFieldName);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("fieldType", fieldType)
                .add("mappingType", mappingType)
                .add("dbFieldName", dbFieldName)
                .toString();
    }
}
