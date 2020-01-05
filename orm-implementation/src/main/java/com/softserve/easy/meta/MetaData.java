package com.softserve.easy.meta;

import com.softserve.easy.meta.field.AbstractMetaField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class MetaData {
    private final Class<?> entityClass;
    private final String entityClassName;

    private final List<Field> fields;

    private final List<Annotation> annotations;
    private final String entityDbName;

    private final Field primaryKey;
    private final Map<Field, AbstractMetaField> metaFields;

    public MetaData(Class<?> entityClass, String entityClassName, List<Field> fields, List<Annotation> annotations,
                    String entityDbName, Field primaryKey, Map<Field, AbstractMetaField> metaFields) {
        this.entityClass = entityClass;
        this.entityClassName = entityClassName;
        this.fields = fields;
        this.annotations = annotations;
        this.entityDbName = entityDbName;
        this.primaryKey = primaryKey;
        this.metaFields = metaFields;
    }
}
