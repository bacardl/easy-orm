package com.softserve.easy.meta;

import java.lang.reflect.Field;
import java.util.List;

public class EmbeddableMetaData {
    private final Class<?> embeddableEntity;
    private final List<Field> fields;

    public EmbeddableMetaData(Class<?> embeddableEntity, List<Field> fields) {
        this.embeddableEntity = embeddableEntity;
        this.fields = fields;
    }

    public Class<?> getEmbeddableEntity() {
        return embeddableEntity;
    }

    public List<Field> getFields() {
        return fields;
    }

}
