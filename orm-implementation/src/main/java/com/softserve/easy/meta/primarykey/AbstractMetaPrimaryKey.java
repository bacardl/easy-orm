package com.softserve.easy.meta.primarykey;

import com.softserve.easy.constant.PrimaryKeyType;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.Injectable;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.Retrievable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractMetaPrimaryKey implements Retrievable<Serializable>, Injectable<Serializable> {
    protected final MetaData entityMetaData;
    protected final PrimaryKeyType primaryKeyType;
    protected final Field field;

    public AbstractMetaPrimaryKey(MetaData entityMetaData, PrimaryKeyType primaryKeyType, Field field) {
        this.entityMetaData = entityMetaData;
        this.primaryKeyType = primaryKeyType;
        this.field = field;
    }

    public PrimaryKeyType getPrimaryKeyType() {
        return primaryKeyType;
    }

    public MetaData getEntityMetaData() {
        return entityMetaData;
    }

    public Field getField() {
        return field;
    }

    @Override
    public Serializable retrieveValue(Object object) throws IllegalAccessException {
        boolean previous = this.field.isAccessible();
        this.field.setAccessible(true);
        Serializable value = (Serializable) this.field.get(object);
        this.field.setAccessible(previous);
        return value;
    }

    @Override
    public void injectValue(Serializable value, Object object) throws IllegalAccessException {
        if (!checkTypeCompatibility(value)) {
            throw new OrmException("Value cannot be injected to the object's field. Reason: incompatibility types.");
        }
        boolean accessible = this.field.isAccessible();
        this.field.setAccessible(true);
        this.field.set(object, value);
        this.field.setAccessible(accessible);
    }


    public abstract Serializable parseIdValue(ResultSet resultSet) throws SQLException;
    public abstract int getNumberOfPrimaryKeys();

    @Override
    public boolean checkTypeCompatibility(Serializable value) {
        return checkIdCompatibility(value.getClass());
    }
    public boolean checkIdCompatibility(Class<?> idClazz) {
        return field.getType().isAssignableFrom(idClazz);
    }

}
