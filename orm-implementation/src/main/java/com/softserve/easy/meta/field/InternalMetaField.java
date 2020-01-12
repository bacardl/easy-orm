package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.softserve.easy.meta.MetaData;

import java.lang.reflect.Field;

public class InternalMetaField extends AbstractMetaField {
    private final String dbFieldName;

    public String getDbFieldName() {
        return dbFieldName;
    }

    public String getDbFieldFullName() {
        return metaData.getEntityDbName() + "." + dbFieldName;
    }

    protected static abstract class Init<T extends Init<T>> extends AbstractMetaField.Init<T> {
        private String dbFieldName;

        public T dbFieldName(String dbFieldName) {
            this.dbFieldName = dbFieldName;
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
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("fieldType", fieldType)
                .add("mappingType", mappingType)
                .add("transitionable", transitionable)
                .add("dbFieldName", dbFieldName)
                .toString();
    }
}
