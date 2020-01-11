package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.softserve.easy.meta.MappingType;

import java.lang.reflect.Field;

public class InternalMetaField extends AbstractMetaField {
    private final String dbFieldName;

    public InternalMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                             Field field,
                             String dbFieldName) {
        super(fieldType, mappingType, transitionable, fieldName, field);
        this.dbFieldName = dbFieldName;
    }

    public String getDbFieldName() {
        return dbFieldName;
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
