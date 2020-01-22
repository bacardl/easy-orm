package com.softserve.easy.meta.field;

import com.softserve.easy.constant.MappingType;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.Injectable;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.Retrievable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractMetaField implements Retrievable<Object>, Injectable<Object> {
    protected final Class<?> fieldType;
    protected final MappingType mappingType;
    protected final String fieldName;

    //required
    protected final Field field;
    protected final MetaData metaData;

    public Class<?> getFieldType() {
        return fieldType;
    }

    public MappingType getMappingType() {
        return mappingType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Field getField() {
        return field;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public boolean checkTypeCompatibility(Object value) {
        return fieldType.isAssignableFrom(value.getClass());
    }

    @Override
    public void injectValue(Object value, Object object) throws IllegalAccessException {
        if (!checkTypeCompatibility(value)) {
            throw new OrmException("Value cannot be injected to the object's field. Reason: incompatibility types.");
        }
        boolean accessible = this.field.isAccessible();
        this.field.setAccessible(true);
        this.field.set(object, value);
        this.field.setAccessible(accessible);
    }

    public abstract Object parseValue(ResultSet resultSet) throws SQLException;


    protected static abstract class Init<T extends Init<T>> {
        protected Class<?> fieldType;
        protected MappingType mappingType;
        protected String fieldName;

        //required
        protected Field field;
        protected MetaData metaData;

        protected abstract T self();

        public T fieldType(Class<?> fieldType) {
            this.fieldType = fieldType;
            return self();
        }

        public T mappingType(MappingType mappingType) {
            this.mappingType = mappingType;
            return self();
        }

        public T fieldName(String fieldName) {
            this.fieldName = fieldName;
            return self();
        }
    }

    protected AbstractMetaField(Init<?> init) {
        this.fieldType = init.fieldType;
        this.mappingType = init.mappingType;
        this.fieldName = init.fieldName;
        this.field = init.field;
        this.metaData = init.metaData;
    }
}
