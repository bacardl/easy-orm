package com.softserve.easy.helper;

import com.softserve.easy.annotation.*;
import com.softserve.easy.exception.ClassValidationException;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MappingType;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.MetaDataBuilder;
import com.softserve.easy.meta.field.AbstractMetaField;
import com.softserve.easy.meta.field.CollectionMetaField;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MetaDataParser {
    public static boolean isEntityAnnotatedClass(Class<?> annotatedClass) {
        Entity annotation = annotatedClass.getAnnotation(Entity.class);
        return Objects.nonNull(annotation);
    }

    public static Optional<Field> getPrimaryKeyField(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (Objects.nonNull(declaredField.getAnnotation(Id.class))) {
                return Optional.of(declaredField);
            }
        }
        return Optional.empty();
    }

    public static Optional<String> getDbTableName(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        Table table = clazz.getAnnotation(Table.class);
        if (Objects.nonNull(table)) {
            return Optional.of(table.name());
        }
        return Optional.empty();
    }

    public static Optional<String> getDbColumnName(Field field) {
        Objects.requireNonNull(field);
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            if (!column.name().isEmpty()) {
                return Optional.of(column.name());
            }
        }
        return Optional.empty();
    }

    /**
     * @throws ClassValidationException
     */
    public static MetaData analyzeClass(Class<?> clazz) {
        MetaDataBuilder metaDataBuilder = new MetaDataBuilder(clazz);

        Optional<Field> primaryKeyField = MetaDataParser.getPrimaryKeyField(clazz);
        metaDataBuilder.setPrimaryKey(primaryKeyField
                .orElseThrow(() -> new ClassValidationException(
                        String.format("Class %s must have field marked with @Id", clazz))));
        Optional<String> entityName = MetaDataParser.getDbTableName(clazz);
        entityName.ifPresent(s -> metaDataBuilder.setEntityDbName(entityName.get()));
        return metaDataBuilder.build();
    }

    /**
     * @throws OrmException
     */
    public static Map<Field, AbstractMetaField> createMetaFields(MetaData metaData) {
        Objects.requireNonNull(metaData.getEntityClass());
        Map<Field, AbstractMetaField> metaFields = new LinkedHashMap<>();
        for (Field field : metaData.getEntityClass().getDeclaredFields()) {
            metaFields.put(field, getMetaField(field, metaData));
        }
        return metaFields;
    }


    /**
     * Factory method
     * @param field
     * @param metaData
     * @return object of Internal, External or Collection + MetaField
     */
    private static AbstractMetaField getMetaField(Field field, MetaData metaData) {
        Class<?> fieldType = field.getType();
        MappingType mappingType = MappingType.getMappingType(fieldType);
        boolean transitionable = isTransientField(field);
        String fieldName = field.getName();
        Optional<String> dbColumnName = getDbColumnName(field);

        switch (mappingType.getFieldType()) {
            case INTERNAL:
                return new InternalMetaField.Builder(field, metaData)
                        .fieldType(fieldType)
                        .mappingType(mappingType)
                        .fieldName(fieldName)
                        .transitionable(transitionable)
                        .dbFieldName(dbColumnName.orElse(fieldName.toLowerCase()))
                        .build();
            case EXTERNAL:
                if (hasOneToOneAnnotation(field) == hasManyToOneAnnotation(field)) {
                    throw new ClassValidationException(String.format("EXTERNAL field %s must have either @OneToOne or @ManyToOne annotation", field));
                }
                return new ExternalMetaField.Builder(field, metaData)
                        .fieldType(fieldType)
                        .mappingType(mappingType)
                        .fieldName(fieldName)
                        .transitionable(transitionable)
                        .foreignKeyFieldName(dbColumnName.orElse(fieldName.toLowerCase()))
                        .build();
            case COLLECTION:
                if (hasManyToManyAnnotation(field) == hasOneToManyAnnotation(field)) {
                    throw new ClassValidationException((String.format("COLLECTION field %s must have either @ManyToMany or @OneToMany annotation", field)));
                }
                Optional<Class<?>> genericTypeOptional = getGenericType(field);
                Class<?> genericType = genericTypeOptional.orElseThrow(
                        () -> new ClassValidationException(String.format("COLLECTION field %s must be parametrized", field)));
                return new CollectionMetaField.Builder(field, metaData)
                        .fieldType(fieldType)
                        .mappingType(mappingType)
                        .fieldName(fieldName)
                        .transitionable(transitionable)
                        .genericType(genericType)
                        .build();
            default:
                throw new OrmException("The framework doesn't support this type of field: " + fieldType);
        }


    }


    public static boolean isTransientField(Field field) {
        return Objects.nonNull(field.getAnnotation(Transient.class));
    }

    public static boolean hasOneToManyAnnotation(Field field) {
        return Objects.nonNull(field.getAnnotation(OneToMany.class));
    }

    public static boolean hasManyToOneAnnotation(Field field) {
        return Objects.nonNull(field.getAnnotation(ManyToOne.class));
    }

    public static boolean hasOneToOneAnnotation(Field field) {
        return Objects.nonNull(field.getAnnotation(OneToOne.class));
    }

    public static boolean hasManyToManyAnnotation(Field field) {
        return Objects.nonNull(field.getAnnotation(ManyToMany.class));
    }

    public static Optional<Class<?>> getGenericType(Field field) {
        ParameterizedType generic = (ParameterizedType) field.getGenericType();
        if (Objects.nonNull(generic)) {
            Class<?> genericActualTypeArgument = (Class<?>) generic.getActualTypeArguments()[0];
            if (Objects.nonNull(genericActualTypeArgument)) {
                return Optional.of(genericActualTypeArgument);
            }
        }
        return Optional.empty();
    }

}