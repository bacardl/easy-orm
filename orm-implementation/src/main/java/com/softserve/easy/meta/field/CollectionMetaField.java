package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.softserve.easy.meta.MappingType;

import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionMetaField  extends AbstractMetaField {
    private final Class<?> genericType;

    public CollectionMetaField(Class<? extends Collection> fieldType, MappingType mappingType, boolean transitionable,
                               String fieldName, Field field,
                               Class<?> genericType) {
        super(fieldType, mappingType, transitionable, fieldName, field);
        this.genericType = genericType;
    }

    public Class<?> getGenericType() {
        return genericType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("fieldType", fieldType)
                .add("mappingType", mappingType)
                .add("transitionable", transitionable)
                .add("genericType", genericType)
                .toString();
    }
}
