package com.softserve.easy.meta;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MappingType {
    INTEGER(Integer.class, "INTEGER"),
    LONG(Long.class, "BIGINT"),
    SHORT(Short.class, "SMALLINT"),
    FLOAT(Float.class, "FLOAT"),
    DOUBLE(Double.class, "DOUBLE"),
    BIG_DECIMAL(BigDecimal.class, "NUMERIC"),
    CHARACTER(Character.class, "CHAR(1)"),
    STRING(String.class, "VARCHAR"),
    DATE(Date.class,"DATE" ),
    COLLECTION(Collection.class, "TABLE"),
    OBJECT(Object.class, "TABLE");

    private Class<?> javaClass;
    private String sqlType ;

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public String getSqlType() {
        return sqlType;
    }

    MappingType(Class<?> javaClass, String sqlType) {
        this.javaClass = javaClass;
        this.sqlType = sqlType;
    }
    public static boolean isInternalType(Class<?> clazz) {
        return getInternalTypes().stream().anyMatch(aClass -> aClass.isAssignableFrom(clazz));
    }

    public static List<Class<?>> getInternalTypes() {
        return Stream.of(INTEGER, LONG, SHORT, FLOAT, DOUBLE, BIG_DECIMAL, CHARACTER, STRING, DATE)
                .map(MappingType::getJavaClass).collect(Collectors.toList());
    }

    public static boolean isCollectionType(Class<?> clazz) {
        return getCollectionTypes().stream().anyMatch(aClass -> aClass.isAssignableFrom(clazz));
    }

    public static List<Class<?>> getCollectionTypes() {
        return Stream.of(COLLECTION).map(MappingType::getJavaClass).collect(Collectors.toList());
    }

    public static boolean isExternalType(Class<?> clazz) {
        return getExternalTypes().stream().anyMatch(aClass -> aClass.isAssignableFrom(clazz));
    }

    public static List<Class<?>> getExternalTypes() {
        return Stream.of(OBJECT).map(MappingType::getJavaClass).collect(Collectors.toList());
    }
}
