package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;

import java.util.Collection;
import java.util.StringJoiner;

public class CollectionMetaField  extends AbstractMetaField {
    private final Class<?> genericType;

    public CollectionMetaField(Class<? extends Collection> fieldType, MappingType mappingType, boolean transitionable, String fieldName,
                               Class<?> genericType) {
        super(fieldType, mappingType, transitionable, fieldName);
        this.genericType = genericType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CollectionMetaField.class.getSimpleName() + "[", "]")
                .add("genericType=" + genericType)
                .add("fieldType=" + fieldType)
                .add("mappingType=" + mappingType)
                .add("transitionable=" + transitionable)
                .add("fieldName='" + fieldName + "'")
                .toString();
    }
}
