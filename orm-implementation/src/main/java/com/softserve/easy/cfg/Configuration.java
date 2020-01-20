package com.softserve.easy.cfg;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.softserve.easy.annotation.Entity;
import com.softserve.easy.constant.FetchType;
import com.softserve.easy.core.SessionFactory;
import com.softserve.easy.core.SessionFactoryImpl;
import com.softserve.easy.exception.ClassValidationException;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.helper.ClassScanner;
import com.softserve.easy.helper.MetaDataParser;
import com.softserve.easy.meta.*;
import com.softserve.easy.meta.field.AbstractMetaField;
import com.softserve.easy.meta.field.CollectionMetaField;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

import static com.softserve.easy.constant.ConfigPropertyConstant.*;
import static com.softserve.easy.helper.MetaDataParser.*;

public class Configuration {
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private Set<Class<?>> observedClasses;
    private Map<Class<?>, MetaData> classConfig;
    private DependencyGraph dependencyGraph;
    private Properties properties;

    private DbSpec dbSpec = new DbSpec();
    private DbSchema dbSchema = dbSpec.getDefaultSchema();

    public Configuration() {
        this.properties = Environment.getProperties();
        this.observedClasses = new HashSet<>();
        this.classConfig = new HashMap<>();
    }

    private void initObservedClasses() {
        String scanPackage = properties.getProperty(ENTITY_PACKAGE_PROPERTY);
        if (Objects.nonNull(scanPackage)) {
            this.observedClasses = ClassScanner.getAnnotatedClasses(Entity.class, scanPackage);
        }
    }

    public Configuration addAnnotatedClass(Class<?> annotatedClass) {
        if (Objects.isNull(annotatedClass)) {
            throw new IllegalArgumentException("The annotatedClass value must be not Null");
        }
        if (!observedClasses.contains(annotatedClass))
        {
            if (MetaDataParser.isEntityAnnotatedClass(annotatedClass))
            {
                observedClasses.add(annotatedClass);
            } else {
                throw new IllegalArgumentException("annotatedClass must be annotated by @Entity");
            }
        } else {
            LOG.info("Class {} has already been mapped from classpath", annotatedClass.getSimpleName());
        }
        return this;
    }

    // creates meta data, creates meta fields and populates(links) meta fields to meta data
    private void addEntityToConfig(Class<?> annotatedClass) {
        MetaData metaData = analyzeClass(annotatedClass);
        Map<Field, AbstractMetaField> metaFields = createMetaFields(metaData);
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
        MetaContext metaContext = getMetaContext();
        return new SessionFactoryImpl(dataSource, metaContext);
    }

    public MetaContext getMetaContext() {
        initObservedClasses();
        String schemaName = properties.getProperty(DB_SCHEMA);
        if (Objects.nonNull(schemaName)) {
            this.dbSchema = dbSpec.createSchema(schemaName);
        }
        initDependencyGraph();
        initClassConfig();
        return new MetaContext(classConfig, dependencyGraph, properties, observedClasses, dbSpec, dbSchema );
    }

    private void initDependencyGraph() {
        this.dependencyGraph = new DependencyGraph(observedClasses);
    }

    private void initClassConfig() {
        for (Class<?> observedClass : observedClasses) {
            addEntityToConfig(observedClass);
        }
    }

    private DataSource initHikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty(DRIVER_CLASS_PROPERTY));
        config.setJdbcUrl(properties.getProperty(URL_PROPERTY));
        config.setUsername(properties.getProperty(USERNAME_PROPERTY));
        config.setPassword(properties.getProperty(PASSWORD_PROPERTY));
        if (Objects.nonNull(properties.getProperty(DB_SCHEMA))) {
//            config.setSchema(properties.getProperty(DB_SCHEMA));
        }
        return new HikariDataSource(config);
    }

    /**
     * @throws ClassValidationException
     */
    public MetaData analyzeClass(Class<?> clazz) {
        MetaDataBuilder metaDataBuilder = new MetaDataBuilder(clazz, this.dbSchema);

        Optional<Field> primaryKeyField = MetaDataParser.getPrimaryKeyField(clazz);
        metaDataBuilder.setPrimaryKey(primaryKeyField
                .orElseThrow(() -> new ClassValidationException(
                        String.format("Class %s must have field marked with @Id", clazz))));
        Optional<String> entityName = MetaDataParser.getDbTableName(clazz);
        entityName.ifPresent(s -> metaDataBuilder.setEntityDbName(entityName.get()));
        return metaDataBuilder.build();
    }

    /**
     * @throws OrmException
     */
    public Map<Field, AbstractMetaField> createMetaFields(MetaData metaData) {
        Objects.requireNonNull(metaData);
        Map<Field, AbstractMetaField> metaFields = new LinkedHashMap<>();
        for (Field field : metaData.getFields()) {
            if (!isTransientField(field)) {
                metaFields.put(field, getMetaField(field, metaData));
            }
        }
        return metaFields;
    }


    /**
     * Factory method
     * @param field
     * @param metaData
     * @return object of Internal, External or Collection + MetaField
     */
    private AbstractMetaField getMetaField(Field field, MetaData metaData) {
        Class<?> fieldType = field.getType();
        MappingType mappingType = MappingType.getMappingType(fieldType);
        String fieldName = field.getName();
        Optional<String> dbColumnName = getDbColumnName(field);
        Optional<FetchType> fetchType = getFetchTypeValue(field);

        switch (mappingType.getFieldType()) {
            case INTERNAL:
                return new InternalMetaField.Builder(field, metaData)
                        .fieldType(fieldType)
                        .mappingType(mappingType)
                        .fieldName(fieldName)
                        .dbFieldName(dbColumnName.orElse(fieldName.toLowerCase()))
                        .setPrimaryKey(MetaDataParser.isPrimaryKeyField(field))
                        .build();
            case EXTERNAL:
                if (hasOneToOneAnnotation(field) == hasManyToOneAnnotation(field)) {
                    throw new ClassValidationException(String.format("EXTERNAL field %s must have either @OneToOne or @ManyToOne annotation", field));
                }
                return new ExternalMetaField.Builder(field, metaData)
                        .fieldType(fieldType)
                        .mappingType(mappingType)
                        .fieldName(fieldName)
                        .foreignKeyFieldName(dbColumnName.orElse(fieldName.toLowerCase()))
                        .fetchType(fetchType.orElse(FetchType.EAGER))
                        .build();
            case COLLECTION:
                if (hasManyToManyAnnotation(field) == hasOneToManyAnnotation(field)) {
                    throw new ClassValidationException((String.format("COLLECTION field %s must have either @ManyToMany or @OneToMany annotation", field)));
                }
                Optional<Class<?>> genericTypeOptional = getGenericType(field);
                Class<?> genericType = genericTypeOptional.orElseThrow(
                        () -> new ClassValidationException(String.format("COLLECTION field %s must be parametrized", field)));
                return new CollectionMetaField.Builder(field, metaData)
                        .fieldType(fieldType)
                        .mappingType(mappingType)
                        .fieldName(fieldName)
                        .genericType(genericType)
                        .collectionFetchType(fetchType.orElse(FetchType.LAZY))
                        .build();
            default:
                throw new OrmException("The framework doesn't support this type of field: " + fieldType);
        }


    }
}
