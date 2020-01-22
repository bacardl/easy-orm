package com.softserve.easy.meta.primarykey;

import com.softserve.easy.constant.PrimaryKeyType;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.InternalMetaField;

public class SinglePrimaryKey extends AbstractMetaPrimaryKey {
    private final InternalMetaField primaryKey;

    public SinglePrimaryKey(InternalMetaField primaryKey, MetaData entityMetaData) {
        super(entityMetaData, PrimaryKeyType.SINGLE);
        this.primaryKey = primaryKey;
    }

    @Override
    public <T> boolean checkIdCompatibility(Class<T> idClazz) {
        return primaryKey.getFieldType().isAssignableFrom(idClazz);
    }

    @Override
    public int getNumberOfPrimaryKeys() {
        return 1;
    }

    public InternalMetaField getPrimaryKey() {
        return primaryKey;
    }

}
