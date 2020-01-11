package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

import java.lang.reflect.Field;

public abstract class AbstractMetaField {
    protected final Class<?> fieldType;
    protected final MappingType mappingType;
    protected final boolean transitionable;
    protected final String fieldName;
    protected final Field field;

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
}
