package com.softserve.easy.jdbc;

import java.math.BigDecimal;
import java.sql.Date;

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
    EXTERNAL(Object.class, "TABLE");

    private Class<?> javaClass;
    private String sqlType ;

    MappingType(Class<?> javaClass, String sqlType) {
        this.javaClass = javaClass;
        this.sqlType = sqlType;
    }
}
