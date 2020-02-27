package com.softserve.easy.bind;

import com.softserve.easy.constant.FetchType;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.helper.ClassScanner;
import com.softserve.easy.jdbc.Persister;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import com.softserve.easy.meta.primarykey.AbstractMetaPrimaryKey;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityBinderImpl implements EntityBinder {
    private static final Logger LOG = LoggerFactory.getLogger(EntityBinderImpl.class);

    private final MetaContext metaContext;
    private final Persister persister;

    public EntityBinderImpl(MetaContext metaContext, Persister persister) {
        this.metaContext = metaContext;
        this.persister = persister;
    }

    @Override
    public <T> Optional<T> buildEntity(Class<T> entityType, ResultSet resultSet) throws Exception {
        MetaData entityMetaData = metaContext.getMetaDataMap().get(entityType);
        AbstractMetaPrimaryKey primaryKey = entityMetaData.getPrimaryKey();
        List<InternalMetaField> internalMetaFields = entityMetaData.getInternalMetaField();
        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();

        final T instance = getInstance(entityMetaData, resultSet);

        // populate pk value (single or composite)
        primaryKey.injectValue(primaryKey.parseIdValue(resultSet), instance);

        for (InternalMetaField metaField : internalMetaFields) {
            metaField.injectValue(metaField.parseValue(resultSet), instance);
        }

        for (ExternalMetaField metaField : externalMetaFields) {
            if (metaField.getEntityFetchType().equals(FetchType.EAGER)) {
                if (Objects.nonNull(metaField.parseValue(resultSet))) {
                    Object childInstance = buildLazyEntity(metaField.getFieldType(), resultSet);
                    metaField.injectValue(childInstance, instance);
                }
            }
        }
        return Optional.ofNullable(instance);
    }

    @Override
    public <T> T buildLazyEntity(Class<T> entityType, ResultSet resultSet) throws Exception {
        MetaData entityMetaData = metaContext.getMetaDataMap().get(entityType);
        AbstractMetaPrimaryKey primaryKey = entityMetaData.getPrimaryKey();
        final T lazyEntity = getLazyInstance(entityMetaData, resultSet);
        primaryKey.injectValue(primaryKey.parseIdValue(resultSet), lazyEntity);
        List<InternalMetaField> internalMetaFields = entityMetaData.getInternalMetaField();
        for (InternalMetaField metaField : internalMetaFields) {
            metaField.injectValue(metaField.parseValue(resultSet), lazyEntity);
        }
        return lazyEntity;
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T getInstance(final MetaData entityMetaData, ResultSet resultSet) throws InstantiationException, IllegalAccessException {
        Class<?> entityType = entityMetaData.getEntityClass();
        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();
        if (hasLazyExternalFields(externalMetaFields)) {
            DynamicType.Builder<?> subclass = new ByteBuddy().subclass(entityType);
            for (ExternalMetaField metaField : externalMetaFields) {
                if (metaField.getEntityFetchType().equals(FetchType.LAZY)) {
                    Optional<Method> getterForField = ClassScanner.getGetterForField(entityType, metaField.getField());
                    if (getterForField.isPresent()) {
                        Serializable fkValue = null;
                        try {
                            fkValue = (Serializable) metaField.parseValue(resultSet);
                        } catch (SQLException e) {
                            LOG.error("Couldn't get fk value from {} column.", metaField.getForeignKeyFieldFullName());
                            throw new OrmException(e);
                        }
                        Method getter = getterForField.get();
                        subclass = subclass.method(ElementMatchers.anyOf(getter))
                                .intercept(MethodDelegation
                                        .to(new LazyLoadingInterceptor(this.persister, metaField, fkValue))
                                        .andThen(SuperMethodCall.INSTANCE));
                    }
                }
            }
            Class<?> proxyClass = subclass.make().load(getClass().getClassLoader()).getLoaded();
            return (T) proxyClass.newInstance();

        }
        return (T) entityType.newInstance();
    }
    @SuppressWarnings(value = "unchecked")
    private <T> T getLazyInstance(final MetaData entityMetaData, ResultSet resultSet) throws IllegalAccessException, InstantiationException {
        Class<?> entityType = entityMetaData.getEntityClass();
        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();
        DynamicType.Builder<?> subclass = new ByteBuddy().subclass(entityType);
        for (ExternalMetaField metaField : externalMetaFields) {
            Optional<Method> getterForField = ClassScanner.getGetterForField(entityType, metaField.getField());
            if (getterForField.isPresent()) {
                Method getter = getterForField.get();
                Serializable fkValue = null;
                try {
                    fkValue = (Serializable) metaField.parseValue(resultSet);
                } catch (SQLException e) {
                    LOG.error("Couldn't get fk value from {} column.", metaField.getForeignKeyFieldFullName());
                    throw new OrmException(e);
                }
                subclass = subclass.method(ElementMatchers.anyOf(getter))
                        .intercept(MethodDelegation
                                .to(new LazyLoadingInterceptor(this.persister, metaField, fkValue))
                                .andThen(SuperMethodCall.INSTANCE));
            }
        }
        Class<?> proxyClass = subclass.make().load(getClass().getClassLoader()).getLoaded();
        return (T) proxyClass.newInstance();
    }

    private boolean hasLazyExternalFields(List<ExternalMetaField> externalMetaFields) {
        return externalMetaFields.stream()
                .anyMatch(externalMetaField -> externalMetaField.getEntityFetchType().equals(FetchType.LAZY));
    }
}
