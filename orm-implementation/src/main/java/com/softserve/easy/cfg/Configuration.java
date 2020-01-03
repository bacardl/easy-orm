package com.softserve.easy.cfg;

import com.softserve.easy.MetaData;
import com.softserve.easy.MetaDataBuilder;
import com.softserve.easy.SessionFactory;
import com.softserve.easy.SessionFactoryBuilder;

import java.util.Map;
import java.util.Properties;

public class Configuration {
    private Map<Class<?>, MetaData> metaData;
    private Properties properties;

    public Configuration() {
        // TODO: load all marked classes using ClassScanner
    }

    public Configuration addAnnotatedClass(Class<?> annotatedClass) {
        if (!metaData.containsKey(annotatedClass))
        {
            // TODO: check if annotated class has @Enity annotation
            // TODO: validate annotated class
            // TODO: create metadata and append to map
        }
        return this;
    }

    public Configuration setProperty(String propertyName, String value) {
        properties.setProperty( propertyName, value );
        return this;
    }

    public Configuration setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    public SessionFactory buildSessionFactory() {
        return new SessionFactoryBuilder(metaData).build();
    }
    private MetaData analyzeClass(Class<?> clazz) {
        MetaDataBuilder metaDataBuilder = new MetaDataBuilder();
        return metaDataBuilder.build();
    }
}
