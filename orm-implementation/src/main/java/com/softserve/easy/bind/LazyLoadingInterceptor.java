package com.softserve.easy.bind;

import com.softserve.easy.jdbc.Persister;
import com.softserve.easy.meta.field.ExternalMetaField;
import net.bytebuddy.implementation.bind.annotation.This;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class LazyLoadingInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(LazyLoadingInterceptor.class);
    private final Persister persister;
    private final Serializable fkValue;
    private final ExternalMetaField metaField;
    private boolean isCalled;

    public LazyLoadingInterceptor(Persister persister, ExternalMetaField metaField, Serializable fkValue) {
        this.persister = persister;
        this.fkValue = fkValue;
        this.metaField = metaField;
    }

    public void intercept(@This Object object) throws Exception {
        if (!isCalled) {
            isCalled = true;
            LOG.info("Loading value to field {} for entity {}", metaField.getFieldName(), metaField.getMetaData().getEntityClass().getSimpleName());
            Object lazyObject = persister.getLazyEntityById(metaField.getFieldType(), fkValue);
            LOG.debug("Loaded lazy object is {}", lazyObject);
            metaField.injectValue(lazyObject, object);
            LOG.debug("Lazy field {} has been defined.", metaField.getFieldType().getName());
            return;
        }
        LOG.info("Field {} has already been initialized.", metaField.getFieldName());

    }
}
