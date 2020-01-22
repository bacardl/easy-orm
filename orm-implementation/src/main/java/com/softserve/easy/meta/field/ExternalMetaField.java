package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.softserve.easy.constant.FetchType;
import com.softserve.easy.meta.MetaData;

import java.lang.reflect.Field;

public class ExternalMetaField extends AbstractMetaField {
    private final String foreignKeyFieldName;
    private final DbColumn externalDbColumn;

    private final FetchType entityFetchType;

    public FetchType getEntityFetchType() {
        return entityFetchType;
    }

    public DbColumn getExternalDbColumn() {
        return externalDbColumn;
    }

    public String getForeignKeyFieldName() {
        return foreignKeyFieldName;
    }

    public String getForeignKeyFieldFullName() {
        return metaData.getEntityDbName() + "." + foreignKeyFieldName;
    }

    @Override
    public Object retrieveValue(Object object) throws IllegalAccessException {
        boolean previous = this.field.isAccessible();
        this.field.setAccessible(true);
        Object value = this.field.get(object);
        this.field.setAccessible(previous);
        return value;
    }



    protected static abstract class Init<T extends Init<T>> extends AbstractMetaField.Init<T> {
        private String foreignKeyFieldName;
        private FetchType fetchType;

        public T foreignKeyFieldName(String foreignKeyFieldName) {
            this.foreignKeyFieldName = foreignKeyFieldName;
            return self();
        }

        public T fetchType(FetchType fetchType) {
            this.fetchType = fetchType;
            return self();
        }

        public ExternalMetaField build() {
            return new ExternalMetaField(this);
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

    protected ExternalMetaField(Init<?> init) {
        super(init);
        this.foreignKeyFieldName = init.foreignKeyFieldName;
        this.externalDbColumn = getMetaData().getDbTable().addColumn(foreignKeyFieldName);
        this.entityFetchType = init.fetchType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("fieldType", fieldType)
                .add("mappingType", mappingType)
                .add("foreignKeyFieldName", foreignKeyFieldName)
                .toString();
    }
}