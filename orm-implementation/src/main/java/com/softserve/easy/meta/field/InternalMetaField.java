package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

import java.util.StringJoiner;

public class InternalMetaField extends AbstractMetaField {
    private final String dbFieldName;

    public InternalMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                             String dbFieldName) {
        super(fieldType, mappingType, transitionable, fieldName);
        this.dbFieldName = dbFieldName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InternalMetaField.class.getSimpleName() + "[", "]")
                .add("dbFieldName='" + dbFieldName + "'")
                .add("fieldType=" + fieldType)
                .add("mappingType=" + mappingType)
                .add("transitionable=" + transitionable)
                .add("fieldName='" + fieldName + "'")
                .toString();
    }
}
