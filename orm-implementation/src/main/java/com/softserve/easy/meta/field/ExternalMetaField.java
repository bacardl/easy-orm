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
