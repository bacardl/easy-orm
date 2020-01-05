package com.softserve.easy.meta;

import com.softserve.easy.exception.OrmException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.softserve.easy.meta.FieldType.*;

public enum MappingType {
    INTEGER(Integer.class, "INTEGER", INTERNAL),
    LONG(Long.class, "BIGINT", INTERNAL),
    SHORT(Short.class, "SMALLINT", INTERNAL),
    FLOAT(Float.class, "FLOAT", INTERNAL),
    DOUBLE(Double.class, "DOUBLE", INTERNAL),
    BIG_DECIMAL(BigDecimal.class, "NUMERIC", INTERNAL),
    CHARACTER(Character.class, "CHAR(1)", INTERNAL),
    STRING(String.class, "VARCHAR", INTERNAL),
    DATE(Date.class,"DATE", INTERNAL),
    LIST(List.class, "TABLE", COLLECTION),
    SET(Set.class, "TABLE", COLLECTION),
    OBJECT(Object.class, "TABLE", EXTERNAL);

    private Class<?> javaClass;
    private String sqlType ;
    private FieldType fieldType;

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public String getSqlType() {
        return sqlType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    MappingType(Class<?> javaClass, String sqlType, FieldType fieldType) {
        this.javaClass = javaClass;
        this.sqlType = sqlType;
        this.fieldType = fieldType;
    }

    public static MappingType getMappingType(Class<?> clazz) {
        for (MappingType value : MappingType.values()) {
            if (value.getJavaClass().isAssignableFrom(clazz)) {
                return value;
            }
        }
        throw new OrmException(String.format("Type %s is not supported!", clazz));
    }

    public static FieldType getFieldType(Class<?> clazz) {
        return getMappingType(clazz).getFieldType();
    }

}
