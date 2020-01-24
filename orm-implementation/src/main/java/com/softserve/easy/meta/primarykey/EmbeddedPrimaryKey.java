package com.softserve.easy.meta.primarykey;

import com.softserve.easy.constant.PrimaryKeyType;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.EmbeddableMetaData;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.InternalMetaField;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmbeddedPrimaryKey extends AbstractMetaPrimaryKey {
    // TODO: make Set<>
    private final List<InternalMetaField> primaryKeys;
    private final EmbeddableMetaData embeddableMetaData;

    public EmbeddedPrimaryKey(EmbeddableMetaData embeddableMetaData, List<InternalMetaField> primaryKeys, MetaData entity,
                              Field field, boolean isGeneratedPk) {
        super(entity, PrimaryKeyType.COMPLEX, field, isGeneratedPk);
        if (primaryKeys.size() < 2) {
            throw new IllegalArgumentException("Embedded primary key must have at least 2 InternalMetaFields.");
        }
        this.primaryKeys = primaryKeys;
        this.embeddableMetaData = embeddableMetaData;
    }

    public List<InternalMetaField> getPrimaryKeys() {
        return primaryKeys;
    }

    public EmbeddableMetaData getEmbeddableMetaData() {
        return embeddableMetaData;
    }

    @Override
    public Serializable parseIdValue(ResultSet resultSet) throws SQLException {
        Class<?> embeddableEntityClass = embeddableMetaData.getEmbeddableEntity();
        Object embeddableId = null;
        try {
            embeddableId = embeddableEntityClass.newInstance();
            for (InternalMetaField primaryKey : primaryKeys) {
                Serializable value = (Serializable) resultSet.getObject(primaryKey.getDbFieldName());
                primaryKey.injectValue(value, embeddableId);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new OrmException("Couldn't instantiate embedded id.", e);
        }
        return (Serializable) embeddableId;
    }

    @Override
    public Serializable parseIdValueByNameColumn(ResultSet resultSet) throws SQLException {
        return parseIdValue(resultSet);
    }

    @Override
    public int getNumberOfPrimaryKeys() {
        return this.primaryKeys.size();
    }

}
