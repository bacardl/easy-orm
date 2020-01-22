package com.softserve.easy.meta.primarykey;

import com.softserve.easy.constant.PrimaryKeyType;
import com.softserve.easy.meta.MetaData;

public abstract class AbstractMetaPrimaryKey {
    private final MetaData entityMetaData;
    private final PrimaryKeyType primaryKeyType;

    public AbstractMetaPrimaryKey(MetaData entityMetaData, PrimaryKeyType primaryKeyType) {
        this.entityMetaData = entityMetaData;
        this.primaryKeyType = primaryKeyType;
    }

    public PrimaryKeyType getPrimaryKeyType() {
        return primaryKeyType;
    }

    public MetaData getEntityMetaData() {
        return entityMetaData;
    }

    public abstract int getNumberOfPrimaryKeys();

    public abstract <T> boolean checkIdCompatibility(Class<T> idClazz);
}
