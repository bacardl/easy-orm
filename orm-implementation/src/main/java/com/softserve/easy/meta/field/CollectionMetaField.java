package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.softserve.easy.meta.MetaData;

import java.lang.reflect.Field;

public class CollectionMetaField  extends AbstractMetaField {
    private final Class<?> genericType;

    public Class<?> getGenericType() {
        return genericType;
    }

    protected static abstract class Init<T extends Init<T>> extends AbstractMetaField.Init<T> {
        private Class<?> genericType;

        public T genericType(Class<?> genericType) {
            this.genericType = genericType;
            return self();
        }

        public CollectionMetaField build() {
            return new CollectionMetaField(this);
        }
    }

    public static class Builder extends Init<Builder> {
        public Builder(Field field, MetaData metaData) {
            this.field = field;
            this.metaData = metaData;
        }
        @Override
        protected Builder self() {
            return this;
        }
    }

    protected CollectionMetaField(Init<?> init) {
        super(init);
        this.genericType = init.genericType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("fieldType", fieldType)
                .add("mappingType", mappingType)
                .add("transitionable", transitionable)
                .add("genericType", genericType)
                .toString();
    }
}
