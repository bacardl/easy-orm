package com.softserve.easy.meta.primarykey;

import com.softserve.easy.constant.PrimaryKeyType;
import com.softserve.easy.meta.EmbeddableMetaData;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.InternalMetaField;

import java.util.List;

public class EmbeddedPrimaryKey extends AbstractMetaPrimaryKey {
    private final List<InternalMetaField> primaryKeys;
    private final EmbeddableMetaData embeddableMetaData;

    public EmbeddedPrimaryKey(EmbeddableMetaData embeddableMetaData, List<InternalMetaField> primaryKeys, MetaData entity) {
        super(entity, PrimaryKeyType.COMPLEX);
        if (primaryKeys.size() < 2) {
            throw new IllegalArgumentException("Embedded primary key must have at least 2 InternalMetaFields.");
        }
        this.primaryKeys = primaryKeys;
        this.embeddableMetaData = embeddableMetaData;
    }

    public List<InternalMetaField> getPrimaryKeys() {
        return primaryKeys;
    }

    @Override
    public <T> boolean checkIdCompatibility(Class<T> idClazz) {
        return embeddableMetaData.getEmbeddableEntity().isAssignableFrom(idClazz);
    }


    @Override
    public int getNumberOfPrimaryKeys() {
        return this.primaryKeys.size();
    }
}
