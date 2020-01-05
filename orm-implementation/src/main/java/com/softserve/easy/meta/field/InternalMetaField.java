package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

public class InternalMetaField extends AbstractMetaField {
    private final String dbFieldName;

    public InternalMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                             String dbFieldName) {
        super(fieldType, mappingType, transitionable, fieldName);
        this.dbFieldName = dbFieldName;
    }
}
