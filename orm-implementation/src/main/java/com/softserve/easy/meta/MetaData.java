package com.softserve.easy.meta;

import com.softserve.easy.meta.field.CollectionMetaField;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;

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
    private final Map<Field, InternalMetaField> columns;
    private final Map<Field, ExternalMetaField> foreignKeys;
    private final Map<Field, CollectionMetaField> collectionFields;

    public MetaData(Class<?> entityClass, String entityClassName, List<Field> fields, List<Annotation> annotations,
                    String entityDbName, Field primaryKey, Map<Field, InternalMetaField> columns,
                    Map<Field, ExternalMetaField> foreignKeys, Map<Field, CollectionMetaField> collectionFields) {
        this.entityClass = entityClass;
        this.entityClassName = entityClassName;
        this.fields = fields;
        this.annotations = annotations;
        this.entityDbName = entityDbName;
        this.primaryKey = primaryKey;
        this.columns = columns;
        this.foreignKeys = foreignKeys;
        this.collectionFields = collectionFields;
    }
}
