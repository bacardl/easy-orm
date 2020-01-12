package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.softserve.easy.meta.MappingType;

import java.lang.reflect.Field;

public class ExternalMetaField extends AbstractMetaField {
    private final String foreignKeyFieldName;

    public ExternalMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                             Field field,
                             String foreignKeyFieldName) {
        super(fieldType, mappingType, transitionable, fieldName, field);
        this.foreignKeyFieldName = foreignKeyFieldName;
    }

    public String getForeignKeyFieldName() {
        return foreignKeyFieldName;
    }

    protected static abstract class Init<T extends Init<T>> extends AbstractMetaField.Init<T> {
        private String foreignKeyFieldName;

        public T foreignKeyFieldName(String foreignKeyFieldName) {
            this.foreignKeyFieldName = foreignKeyFieldName;
            return self();
        }

        public ExternalMetaField build() {
            return new ExternalMetaField(this);
        }
    }

    public static class Builder extends Init<Builder> {
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected ExternalMetaField(Init<?> init) {
        super(init);
        this.foreignKeyFieldName = init.foreignKeyFieldName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("fieldType", fieldType)
                .add("mappingType", mappingType)
                .add("transitionable", transitionable)
                .add("foreignKeyFieldName", foreignKeyFieldName)
                .toString();
    }
}
