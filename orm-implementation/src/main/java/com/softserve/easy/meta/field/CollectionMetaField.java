package com.softserve.easy.meta.field;

import com.google.common.base.MoreObjects;
import com.softserve.easy.constant.FetchType;
import com.softserve.easy.meta.MetaData;

import java.lang.reflect.Field;
import java.util.Collection;

public class CollectionMetaField  extends AbstractMetaField {
    private final Class<?> genericType;
    private final FetchType collectionFetchType;

    public Class<?> getGenericType() {
        return genericType;
    }
    public FetchType getCollectionFetchType() {
        return collectionFetchType;
    }

    @Override
    public Collection<?> retrieveValue(Object object) throws IllegalAccessException {
        boolean previous = this.field.isAccessible();
        this.field.setAccessible(true);
        Collection<?> value = (Collection<?>) this.field.get(object);
        this.field.setAccessible(previous);
        return value;
    }
    protected static abstract class Init<T extends Init<T>> extends AbstractMetaField.Init<T> {
        private Class<?> genericType;
        private FetchType collectionFetchType;

        public T genericType(Class<?> genericType) {
            this.genericType = genericType;
            return self();
        }
        public T collectionFetchType(FetchType collectionFetchType) {
            this.collectionFetchType = collectionFetchType;
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
        this.collectionFetchType = init.collectionFetchType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fieldName", fieldName)
                .add("fieldType", fieldType)
                .add("mappingType", mappingType)
                .add("genericType", genericType)
                .toString();
    }
}
