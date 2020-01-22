package com.softserve.easy.meta;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import com.softserve.easy.meta.field.AbstractMetaField;
import com.softserve.easy.meta.primarykey.AbstractMetaPrimaryKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MetaDataBuilder {
    private final Class<?> entityClass;
    private final String entityClassName;
    private final List<Field> fields;
    private final List<Annotation> annotations;

    private String entityDbName;

    private AbstractMetaPrimaryKey primaryKey;
    private Map<Field, AbstractMetaField> metaFields;

    private DbSchema dbSchema;
    private DbTable dbTable;

    public MetaDataBuilder(Class<?> entityClass, DbSchema dbSchema) {
        this.entityClass = entityClass;
        this.entityClassName = entityClass.getSimpleName().toLowerCase();
        this.fields = Arrays.asList(entityClass.getDeclaredFields());
        this.annotations = Arrays.asList(entityClass.getAnnotations());
        this.dbSchema = dbSchema;
        this.entityDbName = entityClassName;
    }

    public MetaDataBuilder setEntityDbName(String entityDbName) {
        this.entityDbName = entityDbName;
        return this;
    }

    public MetaDataBuilder setPrimaryKey(AbstractMetaPrimaryKey primaryKey) {
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

    public String getEntityDbName() {
        return entityDbName;
    }

    public AbstractMetaPrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public Map<Field, AbstractMetaField> getMetaFields() {
        return metaFields;
    }


    public MetaData build() {
        this.dbTable = this.dbSchema.addTable(getEntityDbName());
        return new MetaData(
                this.entityClass,
                this.entityClassName,
                this.fields,
                this.annotations,
                this.entityDbName,
                this.primaryKey,
                this.metaFields,
                this.dbTable
        );
    }
}