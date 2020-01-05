package com.softserve.easy.helper;

import com.softserve.easy.annotation.*;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MappingType;
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
            return Optional.of(column.name());
        }
        return Optional.empty();
    }

    public static Map<Field, AbstractMetaField> createMetaFields(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        Map<Field, AbstractMetaField> metaFields = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            MappingType mappingType = MappingType.getMappingType(clazz);
            boolean transitionable = isTransientField(field);
            String fieldName = field.getName();
            Optional<String> dbColumnName = getDbColumnName(field);

            switch (mappingType.getFieldType()) {
                case INTERNAL:
                    metaFields.put(field,
                            new InternalMetaField(fieldType, mappingType, transitionable, fieldName,
                                    dbColumnName.orElse(fieldName.toLowerCase()))
                    );
                    break;
                case EXTERNAL:
                    if (hasOneToOneAnnotation(field) ^ hasManyToOneAnnotation(field)) {
                        throw new OrmException(String.format("EXTERNAL field %s must have either @OneToOne or @ManyToOne annotation", field));
                    }
                    metaFields.put(field,
                            new ExternalMetaField(fieldType, mappingType, transitionable, fieldName,
                                    dbColumnName.orElse(fieldName.toLowerCase()))
                    );
                    break;
                case COLLECTION:
                    if (hasManyToManyAnnotation(field) ^ hasOneToManyAnnotation(field)) {
                        throw new OrmException(String.format("COLLECTION field %s must have either @ManyToMany or @OneToMany annotation", field));
                    }
                    Optional<Class<?>> genericTypeOptional = getGenericType(field);
                    Class<?> genericType = genericTypeOptional.orElseThrow(
                            () -> new OrmException(String.format("COLLECTION field %s must be parametrized", field)));
                    metaFields.put(field,
                            new CollectionMetaField(fieldType, mappingType, transitionable, fieldName, genericType)
                    );
                    break;
                default:
                    throw new OrmException("The framework doesn't support this type of field: " + fieldType);
            }


        }
        return metaFields;
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
