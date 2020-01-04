package com.softserve.easy.meta;

import com.softserve.easy.meta.field.CollectionMetaField;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaDataBuilder {
    private final Class<?> entityClass;
    private final String entityClassName;
    private final List<Field> fields;
    private final List<Annotation> annotations;

    private String entityDbName;

    private Field primaryKey;
    private Map<Field, InternalMetaField> columns;
    private Map<Field, ExternalMetaField> foreignKeys;
    private Map<Field, CollectionMetaField> collectionFields;

    public MetaDataBuilder(Class<?> entityClass) {
        this.entityClass = entityClass;
        this.entityClassName = entityClass.getSimpleName().toLowerCase();
        this.fields = Arrays.asList(entityClass.getDeclaredFields());
        this.annotations = Arrays.asList(entityClass.getAnnotations());

        this.entityDbName = entityClassName;
        this.columns = new HashMap<>();
        this.foreignKeys = new HashMap<>();
        this.collectionFields = new HashMap<>();
    }

    public MetaDataBuilder setEntityDbName(String entityDbName) {
        this.entityDbName = entityDbName;
        return this;
    }

    public MetaDataBuilder setPrimaryKey(Field primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public MetaDataBuilder setColumns(Map<Field, InternalMetaField> columns) {
        this.columns = columns;
        return this;
    }

    public MetaDataBuilder setForeignKeys(Map<Field, ExternalMetaField> foreignKeys) {
        this.foreignKeys = foreignKeys;
        return this;
    }

    public MetaDataBuilder setCollectionFields(Map<Field, CollectionMetaField> collectionFields) {
        this.collectionFields = collectionFields;
        return this;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public String getEntityDbName() {
        return entityDbName;
    }

    public Field getPrimaryKey() {
        return primaryKey;
    }

    public Map<Field, InternalMetaField> getColumns() {
        return columns;
    }

    public Map<Field, ExternalMetaField> getForeignKeys() {
        return foreignKeys;
    }

    public Map<Field, CollectionMetaField> getCollectionFields() {
        return collectionFields;
    }

    public MetaData build() {
        return new MetaData(
                this.entityClass,
                this.entityClassName,
                this.fields,
                this.annotations,
                this.entityDbName,
                this.primaryKey,
                this.columns,
                this.foreignKeys,
                this.collectionFields
        );
    }
}
