package com.softserve.easy.helper;

import com.softserve.easy.annotation.*;
import com.softserve.easy.constant.FetchType;
import com.softserve.easy.constant.PrimaryKeyType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.Optional;

public class MetaDataParser {
    public static boolean isEntityAnnotatedClass(Class<?> annotatedClass) {
        Entity annotation = annotatedClass.getAnnotation(Entity.class);
        return Objects.nonNull(annotation);
    }

    public static boolean isEmbeddableEntityAnnotatedClass(Class<?> annotatedClass) {
        Embeddable annotation = annotatedClass.getAnnotation(Embeddable.class);
        return Objects.nonNull(annotation);
    }

    public static boolean isTransientField(Field field) {
        return Objects.nonNull(field.getAnnotation(Transient.class));
    }

    public static boolean isPrimaryKeyField(Field field) {
        return Objects.nonNull(field.getAnnotation(Id.class)) ||
                Objects.nonNull(field.getAnnotation(EmbeddedId.class));
    }

    public static boolean isGeneratedPk(Field pkField) {
        return Objects.nonNull(pkField.getAnnotation(GeneratedValue.class));
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

    public static boolean hasJoinColumnAnnotation(Field field) {
        return Objects.nonNull(field.getAnnotation(JoinColumn.class));
    }

    public static boolean hasPrimaryKeyJoinColumnAnnotation(Field field) {
        return Objects.nonNull(field.getAnnotation(PrimaryKeyJoinColumn.class));
    }

    public static boolean hasMapsIdAnnotation(Field field) {
        return Objects.nonNull(field.getAnnotation(MapsId.class));
    }

    public static Optional<Field> getPrimaryKeyField(Class<?> clazz) {
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (isPrimaryKeyField(declaredField)) {
                return Optional.of(declaredField);
            }
        }
        return Optional.empty();
    }

    public static Optional<String> getDbTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (Objects.nonNull(table)) {
            return Optional.of(table.name());
        }
        return Optional.empty();
    }

    public static Optional<String> getDbColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (Objects.nonNull(column)) {
            if (!column.name().isEmpty()) {
                return Optional.of(column.name());
            }
        }
        return Optional.empty();
    }

    public static Optional<FetchType> getFetchTypeValue(Field field) {
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);

        if (Objects.nonNull(manyToOne)) {
            return Optional.of(manyToOne.fetch());
        }

        if (Objects.nonNull(oneToOne)) {
            return Optional.of(oneToOne.fetch());
        }

        if (Objects.nonNull(oneToMany)) {
            return Optional.of(oneToMany.fetch());
        }

        return Optional.empty();
    }

    public static Optional<PrimaryKeyType> getPrimaryKeyType(Field field) {
        if (Objects.nonNull(field.getAnnotation(Id.class))) {
            return Optional.of(PrimaryKeyType.SINGLE);
        }
        if (Objects.nonNull(field.getAnnotation(EmbeddedId.class))) {
            return Optional.of(PrimaryKeyType.COMPLEX);
        }
        return Optional.empty();
    }

    public static Optional<String> getJoinColumnName(Field field) {
        JoinColumn joinedColumn = field.getAnnotation(JoinColumn.class);
        if (Objects.nonNull(joinedColumn)) {
            if (!joinedColumn.name().isEmpty()) {
                return Optional.of(joinedColumn.name());
            }
        }
        return Optional.empty();
    }

    public static Optional<String> getMapsIdColumnName(Field field) {
        MapsId mapsId = field.getAnnotation(MapsId.class);
        if (Objects.nonNull(mapsId)) {
            if (!mapsId.value().isEmpty()) {
                return Optional.of(mapsId.value());
            }
        }
        return Optional.empty();
    }

    public static Optional<String> getPrimaryKeyJoinColumnName(Field field) {
        PrimaryKeyJoinColumn mapsId = field.getAnnotation(PrimaryKeyJoinColumn.class);
        if (Objects.nonNull(mapsId)) {
            if (!mapsId.name().isEmpty()) {
                return Optional.of(mapsId.name());
            }
        }
        return Optional.empty();
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