package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

import java.util.StringJoiner;

public class ExternalMetaField extends AbstractMetaField {
    private final String foreignKeyFieldName;

    public ExternalMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                                String foreignKeyFieldName) {
        super(fieldType, mappingType, transitionable, fieldName);
        this.foreignKeyFieldName = foreignKeyFieldName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExternalMetaField.class.getSimpleName() + "[", "]")
                .add("foreignKeyFieldName='" + foreignKeyFieldName + "'")
                .add("fieldType=" + fieldType)
                .add("mappingType=" + mappingType)
                .add("transitionable=" + transitionable)
                .add("fieldName='" + fieldName + "'")
                .toString();
    }
}
