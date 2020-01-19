package com.softserve.easy.meta.field;

import com.softserve.easy.meta.MappingType;
import com.softserve.easy.meta.MetaData;

import java.lang.reflect.Field;

public abstract class AbstractMetaField {
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
