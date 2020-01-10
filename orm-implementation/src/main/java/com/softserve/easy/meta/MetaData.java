package com.softserve.easy.meta;

import com.softserve.easy.meta.field.AbstractMetaField;
import com.softserve.easy.meta.field.CollectionMetaField;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetaData {
    private final Class<?> entityClass;
    private final String entityClassName;

    private final List<Field> fields;

    private final List<Annotation> annotations;
    private final String entityDbTableName;

    private final Field primaryKey;
    private final Map<Field, AbstractMetaField> metaFields;

    public MetaData(Class<?> entityClass, String entityClassName, List<Field> fields, List<Annotation> annotations,
                    String entityDbTableName, Field primaryKey, Map<Field, AbstractMetaField> metaFields) {
        this.entityClass = entityClass;
        this.entityClassName = entityClassName;
        this.fields = fields;
        this.annotations = annotations;
        this.entityDbTableName = entityDbTableName;
        this.primaryKey = primaryKey;
        this.metaFields = metaFields;
    }

    public List<InternalMetaField> getInternalMetaField() {
        return metaFields.values().stream()
                .filter(abstractMetaField -> abstractMetaField.getMappingType().getFieldType().equals(FieldType.INTERNAL))
                .map(abstractMetaField -> (InternalMetaField)abstractMetaField)
                .collect(Collectors.toList());
    }

    public List<ExternalMetaField> getExternalMetaField() {
        return metaFields.values().stream()
                .filter(abstractMetaField -> abstractMetaField.getMappingType().getFieldType().equals(FieldType.EXTERNAL))
                .map(abstractMetaField -> (ExternalMetaField)abstractMetaField)
                .collect(Collectors.toList());
    }

    public List<CollectionMetaField> getCollectionMetaField() {
        return metaFields.values().stream()
                .filter(abstractMetaField -> abstractMetaField.getMappingType().getFieldType().equals(FieldType.COLLECTION))
                .map(abstractMetaField -> (CollectionMetaField)abstractMetaField)
                .collect(Collectors.toList());
    }

    public <T> boolean checkIdCompatibility(Class<T> idClazz) {
        return metaFields.get(primaryKey).getFieldType().equals(idClazz);
    }

    public <T> boolean checkTypeCompatibility(Class<T> entityType) {
        return entityClass.equals(entityType);
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

}
