package com.softserve.easy.meta;

import com.softserve.easy.meta.field.AbstractMetaField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetaDataBuilder {
    private final Class<?> entityClass;
    private final String entityClassName;
    private final List<Field> fields;
    private final List<Annotation> annotations;

    private String entityDbTableName;

    private Field primaryKey;
    private Map<Field, AbstractMetaField> metaFields;

    public MetaDataBuilder(Class<?> entityClass) {
        this.entityClass = entityClass;
        this.entityClassName = entityClass.getSimpleName().toLowerCase();
        this.fields = Arrays.asList(entityClass.getDeclaredFields());
        this.annotations = Arrays.asList(entityClass.getAnnotations());

        this.entityDbTableName = entityClassName;
        this.metaFields = new LinkedHashMap<>();
    }

    public MetaDataBuilder setEntityDbTableName(String entityDbTableName) {
        this.entityDbTableName = entityDbTableName;
        return this;
    }

    public MetaDataBuilder setPrimaryKey(Field primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public MetaDataBuilder setMetaFields(Map<Field, AbstractMetaField> metaFields) {
        this.metaFields = metaFields;
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

    public String getEntityDbTableName() {
        return entityDbTableName;
    }

    public Field getPrimaryKey() {
        return primaryKey;
    }

    public Map<Field, AbstractMetaField> getMetaFields() {
        return metaFields;
    }


    public MetaData build() {
        return new MetaData(
                this.entityClass,
                this.entityClassName,
                this.fields,
                this.annotations,
                this.entityDbTableName,
                this.primaryKey,
                this.metaFields
        );
    }
}
