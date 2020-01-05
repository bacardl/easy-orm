package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

public abstract class AbstractMetaField {
    protected final Class<?> fieldType;
    protected final MappingType mappingType;
    protected final boolean transitionable;
    protected final String fieldName;

    protected AbstractMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName) {
        this.fieldType = fieldType;
        this.mappingType = mappingType;
        this.transitionable = transitionable;
        this.fieldName = fieldName;
    }

    protected Class<?> runtimeType;

    public void setRuntimeType(Class<?> runtimeType) {
        this.runtimeType = runtimeType;
    }

    public Class<?> getRuntimeType() {
        return runtimeType;
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
}
