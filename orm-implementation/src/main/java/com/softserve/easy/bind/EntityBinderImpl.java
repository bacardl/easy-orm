package com.softserve.easy.bind;

import com.softserve.easy.constant.FetchType;
import com.softserve.easy.helper.ClassScanner;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.*;

public class EntityBinderImpl implements EntityBinder {
    private final Map<Class<?>, Class<?>> entityProxyMap = new HashMap<>();
    private final Map<Class<?>, Class<?>> entityLazyProxyMap = new HashMap<>();
    private final MetaContext metaContext;

    public EntityBinderImpl(MetaContext metaContext) {
        this.metaContext = metaContext;
    }

    @Override
    public <T> Optional<T> buildEntity(Class<T> entityType, ResultSet resultSet) throws Exception {
        MetaData entityMetaData = metaContext.getMetaDataMap().get(entityType);
        List<InternalMetaField> internalMetaFields = entityMetaData.getInternalMetaField();
        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();

        final T instance = getInstance(entityMetaData);
        for (InternalMetaField metaField : internalMetaFields) {
            Field field = metaField.getField();
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(instance, resultSet.getObject(metaField.getDbFieldName(), metaField.getFieldType()));
            field.setAccessible(accessible);
        }

        for (ExternalMetaField metaField : externalMetaFields) {
            if (metaField.getEntityFetchType().equals(FetchType.EAGER)) {
                Field field = metaField.getField();
                Object childInstance = null;
                if (Objects.nonNull(resultSet.getObject(metaField.getForeignKeyFieldFullName()))) {
                    childInstance = buildLazyEntity(metaField.getFieldType(), resultSet);
                }
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                field.set(instance, childInstance);
                field.setAccessible(accessible);
            }
        }
        return Optional.ofNullable(instance);
    }

    public <T> T buildLazyEntity(Class<T> entityType, ResultSet resultSet) throws Exception {
        MetaData entityMetaData = metaContext.getMetaDataMap().get(entityType);
        final T lazyEntity = getLazyInstance(entityMetaData);
        List<InternalMetaField> internalMetaFields = entityMetaData.getInternalMetaField();
        for (InternalMetaField metaField : internalMetaFields) {
            Field field = metaField.getField();
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(lazyEntity, resultSet.getObject(metaField.getDbFieldName(), metaField.getFieldType()));
            field.setAccessible(accessible);
        }
        return lazyEntity;
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T getInstance(final MetaData entityMetaData) throws InstantiationException, IllegalAccessException {
        Class<?> entityType = entityMetaData.getEntityClass();
        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();
        if (hasLazyExternalFields(externalMetaFields)) {
            if (entityProxyMap.containsKey(entityType)) {
                return (T) entityProxyMap.get(entityType).newInstance();
            } else {
                DynamicType.Builder<?> subclass = new ByteBuddy().subclass(entityType);
                for (ExternalMetaField metaField : externalMetaFields) {
                    if (metaField.getEntityFetchType().equals(FetchType.LAZY)) {
                        Optional<Method> getterForField = ClassScanner.getGetterForField(entityType, metaField.getField());
                        if (getterForField.isPresent()) {
                            Method getter = getterForField.get();
                            subclass = subclass.method(ElementMatchers.anyOf(getter))
                                    .intercept(MethodDelegation
                                            .to(new LazyLoadingInterceptor())
                                            .andThen(SuperMethodCall.INSTANCE));
                        }
                    }
                }
                Class<?> proxyClass = subclass.make().load(getClass().getClassLoader()).getLoaded();
                entityProxyMap.put(entityType, proxyClass);
                return (T) proxyClass.newInstance();
            }
        }
        return (T) entityType.newInstance();
    }

    private boolean hasLazyExternalFields(List<ExternalMetaField> externalMetaFields) {
        return externalMetaFields.stream()
                .anyMatch(externalMetaField -> externalMetaField.getEntityFetchType().equals(FetchType.LAZY));
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T getLazyInstance(final MetaData entityMetaData) throws IllegalAccessException, InstantiationException {
        Class<?> entityType = entityMetaData.getEntityClass();
        if (entityLazyProxyMap.containsKey(entityType)) {
            return (T) entityLazyProxyMap.get(entityType);
        }
        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();
        DynamicType.Builder<?> subclass = new ByteBuddy().subclass(entityType);
        for (ExternalMetaField metaField : externalMetaFields) {
            Optional<Method> getterForField = ClassScanner.getGetterForField(entityType, metaField.getField());
            if (getterForField.isPresent()) {
                Method getter = getterForField.get();
                subclass = subclass.method(ElementMatchers.anyOf(getter))
                        .intercept(MethodDelegation
                                .to(new LazyLoadingInterceptor())
                                .andThen(SuperMethodCall.INSTANCE));
            }
        }
        Class<?> proxyClass = subclass.make().load(getClass().getClassLoader()).getLoaded();
        entityLazyProxyMap.put(entityType, proxyClass);
        return (T) proxyClass.newInstance();
    }
}
