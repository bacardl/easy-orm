package com.softserve.easy.cfg;

import com.softserve.easy.annotation.Entity;
import com.softserve.easy.core.SessionFactory;
import com.softserve.easy.core.SessionFactoryImpl;
import com.softserve.easy.helper.ClassScanner;
import com.softserve.easy.helper.MetaDataParser;
import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.AbstractMetaField;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

import static com.softserve.easy.cfg.ConfigPropertyConstant.*;

public class Configuration {
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private Set<Class<?>> observedClasses;
    private Map<Class<?>, MetaData> classConfig;
    private DependencyGraph dependencyGraph;
    private Properties properties;

    public Configuration() {
        this.properties = Environment.getProperties();
        this.observedClasses = ClassScanner.getAnnotatedClasses(Entity.class,
                Objects.requireNonNull(properties.getProperty(ENTITY_PACKAGE_PROPERTY)));
        this.classConfig = new HashMap<>();

        for (Class<?> observedClass : observedClasses) {
            addEntityToConfig(observedClass);
        }
        this.dependencyGraph = new DependencyGraph(observedClasses);
    }

    public Configuration addAnnotatedClass(Class<?> annotatedClass) {
        if (Objects.isNull(annotatedClass)) {
            throw new IllegalArgumentException("The annotatedClass value must be not Null");
        }
        if (!classConfig.containsKey(annotatedClass))
        {
            if (MetaDataParser.isEntityAnnotatedClass(annotatedClass))
            {
                addEntityToConfig(annotatedClass);
            } else {
                throw new IllegalArgumentException("annotatedClass must be annotated by @Entity");
            }
        } else {
            LOG.info("Class {} has already been mapped from classpath", annotatedClass.getSimpleName());
        }
        dependencyGraph = new DependencyGraph(observedClasses);
        return this;
    }

    // creates meta data, creates meta fields and populates(links) meta fields to meta data
    private void addEntityToConfig(Class<?> annotatedClass) {
        MetaData metaData = MetaDataParser.analyzeClass(annotatedClass);
        Map<Field, AbstractMetaField> metaFields = MetaDataParser.createMetaFields(metaData);
        metaData.setMetaFields(metaFields);
        classConfig.put(annotatedClass, metaData);
    }


    public Configuration setProperty(String propertyName, String value) {
        properties.setProperty(propertyName, value);
        return this;
    }

    public Configuration addProperties(Properties properties) {
        this.properties.putAll(properties);
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * @return SessionFactory with the same static state(configuration),
     * but with a new DataSource instance
     */
    public SessionFactory buildSessionFactory() {
        DataSource dataSource = initHikariDataSource();
        MetaContext metaContext = new MetaContext(classConfig, dependencyGraph, properties, observedClasses);
        return new SessionFactoryImpl(dataSource, metaContext);
    }

    private DataSource initHikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty(DRIVER_CLASS_PROPERTY));
        config.setJdbcUrl(properties.getProperty(URL_PROPERTY));
        config.setUsername(properties.getProperty(USERNAME_PROPERTY));
        config.setPassword(properties.getProperty(PASSWORD_PROPERTY));
        return new HikariDataSource(config);
    }
}
