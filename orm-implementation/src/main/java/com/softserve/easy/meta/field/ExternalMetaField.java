package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

public class ExternalMetaField extends AbstractMetaField {
    private final String foreignKeyFieldName;

    public ExternalMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                                String foreignKeyFieldName) {
        super(fieldType, mappingType, transitionable, fieldName);
        this.foreignKeyFieldName = foreignKeyFieldName;
    }
}
