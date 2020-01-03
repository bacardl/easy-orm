package com.softserve.easy.cfg;

import com.softserve.easy.annotation.Entity;
import com.softserve.easy.core.SessionFactory;
import com.softserve.easy.core.SessionFactoryBuilder;
import com.softserve.easy.helper.ClassScanner;
import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.MetaDataBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.*;

import static com.softserve.easy.cfg.ConfigPropertyConstant.*;

public class Configuration {
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private Set<Class<?>> observedClasses;
    private Map<Class<?>, MetaData> metaData;
    private DependencyGraph dependencyGraph;
    private Properties properties;

    public Configuration() {
        this.properties = Environment.getProperties();
        this.observedClasses = ClassScanner.getAnnotatedClasses(Entity.class);
        this.metaData = new HashMap<>();

        for (Class<?> observedClass : observedClasses) {
            // metaData.put(observedClass, analyzeClass(observedClass));
        }
    }

    public Configuration addAnnotatedClass(Class<?> annotatedClass) {
        if (Objects.isNull(annotatedClass)) {
            throw new IllegalArgumentException("The annotatedClass value must be not Null");
        }
        if (!metaData.containsKey(annotatedClass))
        {
            if (isEntityAnnotatedClass(annotatedClass))
            {
                // metadata.put(observedClass, analyzeClass(annotatedClass))
            } else {
                throw new IllegalArgumentException("annotatedClass must be annotated by @Entity");
            }
        } else {
            LOG.info("Class {} has already been mapped from classpath", annotatedClass.getSimpleName());
        }

        // TODO: recompute the metaData and graphDependency fields
        return this;
    }

    private boolean isEntityAnnotatedClass(Class<?> annotatedClass) {
        Entity annotation = annotatedClass.getAnnotation(Entity.class);
        return Objects.nonNull(annotation);
    }

    public Configuration setProperty(String propertyName, String value) {
        properties.setProperty( propertyName, value );
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
        DataSource dataSource = initDataSource();
        return new SessionFactoryBuilder()
                .setDataSource(dataSource)
                .setMetaDataMap(metaData)
                .setDependencyGraph(dependencyGraph)
                .build();
    }

    private DataSource initDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty(DRIVER_CLASS_PROPERTY));
        config.setJdbcUrl(properties.getProperty(URL_PROPERTY));
        config.setUsername(properties.getProperty(USERNAME_PROPERTY));
        config.setPassword(properties.getProperty(PASSWORD_PROPERTY));
        return new HikariDataSource(config);
    }

    private MetaData analyzeClass(Class<?> clazz) {
        MetaDataBuilder metaDataBuilder = new MetaDataBuilder();
        return metaDataBuilder.build();
    }
}
