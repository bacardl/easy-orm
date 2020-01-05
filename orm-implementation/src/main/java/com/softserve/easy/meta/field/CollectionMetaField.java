package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

public class CollectionMetaField  extends AbstractMetaField {
    private final Class<?> genericType;

    public CollectionMetaField(Class<?> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                               Class<?> genericType) {
        super(fieldType, mappingType, transitionable, fieldName);
        this.genericType = genericType;
    }
}
