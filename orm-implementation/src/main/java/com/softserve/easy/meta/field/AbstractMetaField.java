package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;
import com.softserve.easy.meta.MetaData;

import java.lang.reflect.Field;

public abstract class AbstractMetaField {
    protected final Class<?> fieldType;
    protected final MappingType mappingType;
    protected final boolean transitionable;
    protected final String fieldName;
    protected final Field field;
    protected MetaData metaData; // must be final

    protected AbstractMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName, Field field) {

        this.fieldType = fieldType;
        this.mappingType = mappingType;
        this.transitionable = transitionable;
        this.fieldName = fieldName;
        this.field = field;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }
    public MappingType getMappingType() {
        return mappingType;
    }
    public boolean isTransitionable() {
        return transitionable;
    }
    public String getFieldName() {
        return fieldName;
    }
    public Field getField() {
        return field;
    }

    protected static abstract class Init<T extends Init<T>> {
        protected Class<?> fieldType;
        protected MappingType mappingType;
        protected boolean transitionable;
        protected String fieldName;
        protected Field field;
        protected MetaData metaData;

        protected abstract T self();

        public T transitionable(boolean transitionable) {
            this.transitionable = transitionable;
            return self();
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

    protected AbstractMetaField(Init<?> init) {
        this.fieldType = init.fieldType;
        this.mappingType = init.mappingType;
        this.transitionable = init.transitionable;
        this.fieldName = init.fieldName;
        this.field = init.field;
        this.metaData = init.metaData;
    }
}
