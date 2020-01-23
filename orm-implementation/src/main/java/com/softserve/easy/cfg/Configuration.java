package com.softserve.easy.cfg;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.softserve.easy.annotation.Embeddable;
import com.softserve.easy.annotation.Entity;
import com.softserve.easy.constant.FetchType;
import com.softserve.easy.constant.FieldType;
import com.softserve.easy.constant.MappingType;
import com.softserve.easy.constant.PrimaryKeyType;
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
import com.softserve.easy.meta.primarykey.AbstractMetaPrimaryKey;
import com.softserve.easy.meta.primarykey.EmbeddedPrimaryKey;
import com.softserve.easy.meta.primarykey.SinglePrimaryKey;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.softserve.easy.constant.ConfigPropertyConstant.*;
import static com.softserve.easy.helper.MetaDataParser.*;

public class Configuration {
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private Set<Class<?>> observedEntities;
    private Set<Class<?>> observedEmbeddableEntities;
    private Map<Class<?>, MetaData> entityConfig;
    private Map<Class<?>, EmbeddableMetaData> embeddedEntityConfig;
    private DependencyGraph dependencyGraph;
    private Properties properties;


    private DbSpec dbSpec = new DbSpec();
    private DbSchema dbSchema = dbSpec.getDefaultSchema();

    public Configuration() {
        this.properties = Environment.getProperties();
        this.observedEntities = new HashSet<>();
        this.observedEmbeddableEntities = new HashSet<>();
        this.entityConfig = new HashMap<>();
        this.embeddedEntityConfig = new HashMap<>();
    }

    private void initObservedClasses() {
        String scanPackage = properties.getProperty(ENTITY_PACKAGE_PROPERTY);
        if (Objects.nonNull(scanPackage)) {
            this.observedEntities = ClassScanner.getAnnotatedClasses(Entity.class, scanPackage);
            this.observedEmbeddableEntities = ClassScanner.getAnnotatedClasses(Embeddable.class, scanPackage);
        }
    }

    public Configuration addAnnotatedClass(Class<?> annotatedClass) {
        if (Objects.isNull(annotatedClass)) {
            throw new IllegalArgumentException("The annotatedClass value must be not Null");
        }
        if (!observedEntities.contains(annotatedClass)) {
            if (MetaDataParser.isEntityAnnotatedClass(annotatedClass)) {
                observedEntities.add(annotatedClass);
            } else if (MetaDataParser.isEmbeddableEntityAnnotatedClass(annotatedClass)) {
                observedEmbeddableEntities.add(annotatedClass);
            } else {
                throw new IllegalArgumentException("annotatedClass must be annotated by @Entity or @Embeddable");
            }
        } else {
            LOG.info("Class {} has already been mapped from classpath", annotatedClass.getSimpleName());
        }
        return this;
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
        initMetaConfig();
        return new MetaContext(entityConfig, embeddedEntityConfig, dependencyGraph, properties, observedEntities,
                observedEmbeddableEntities, dbSpec, dbSchema);
    }

    private void initMetaConfig() {
        initEmbeddableEntityConfig();
        initEntityConfig();
        initDependencyGraph();
    }

    private void initEmbeddableEntityConfig() {
        for (Class<?> observedEmbeddableEntity : observedEmbeddableEntities) {
            addEmbeddableEntityToConfig(observedEmbeddableEntity);
        }
    }

    private void initEntityConfig() {
        for (Class<?> observedEntity : observedEntities) {
            addEntityToConfig(observedEntity);
        }
    }

    private void initDependencyGraph() {
        this.dependencyGraph = new DependencyGraph(observedEntities);
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

    // creates a meta data, creates meta fields and populates(links) meta fields to meta data
    private void addEntityToConfig(Class<?> annotatedClass) {
        MetaData metaData = analyzeEntityClass(annotatedClass);
        Map<Field, AbstractMetaField> metaFields = createMetaFields(metaData);
        metaData.setMetaFields(metaFields);
        Field primaryKeyField = getPrimaryKeyField(annotatedClass)
                .orElseThrow(() -> new ClassValidationException("Entity class must have an primary key field."));
        AbstractMetaPrimaryKey primaryKey = getMetaPrimaryKey(primaryKeyField, metaData);
        metaData.setPrimaryKey(primaryKey);
        entityConfig.put(annotatedClass, metaData);
    }

    private void addEmbeddableEntityToConfig(Class<?> annotatedClass) {
        EmbeddableMetaData embeddableMetaData = analyzeEmbeddableClass(annotatedClass);
        embeddedEntityConfig.put(annotatedClass, embeddableMetaData);
    }

    /**
     * @throws ClassValidationException
     */
    public MetaData analyzeEntityClass(Class<?> entityClass) {
        MetaDataBuilder metaDataBuilder = new MetaDataBuilder(entityClass, this.dbSchema);
        Optional<String> entityName = MetaDataParser.getDbTableName(entityClass);
        entityName.ifPresent(s -> metaDataBuilder.setEntityDbName(entityName.get()));
        return metaDataBuilder.build();
    }

    private EmbeddableMetaData analyzeEmbeddableClass(Class<?> annotatedClass) {
        Field[] declaredFields = annotatedClass.getDeclaredFields();
        List<Field> nonStaticDeclaredFields = Arrays.stream(declaredFields)
                .filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
        boolean isAllInternalFields = nonStaticDeclaredFields.stream()
                .allMatch(field ->
                        MappingType.getFieldType(field.getType()).equals(FieldType.INTERNAL));
        if (!isAllInternalFields) {
            throw new ClassValidationException("Embeddable class must have only INTERNAL fields");
        }
        return new EmbeddableMetaData(annotatedClass, nonStaticDeclaredFields);
    }

    /**
     * @throws OrmException
     */
    public Map<Field, AbstractMetaField> createMetaFields(MetaData metaData) {
        Objects.requireNonNull(metaData);
        Map<Field, AbstractMetaField> metaFields = new LinkedHashMap<>();
        for (Field field : metaData.getFields()) {
            if (!isTransientField(field)) {
                if (!isPrimaryKeyField(field)) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        metaFields.put(field, initMetaField(field, metaData));
                    }
                }
            }
        }
        return metaFields;
    }

    private AbstractMetaPrimaryKey getMetaPrimaryKey(Field pkField, MetaData metaData) {
        PrimaryKeyType primaryKeyType = MetaDataParser.getPrimaryKeyType(pkField)
                .orElseThrow(() -> new ClassValidationException("The field " + pkField.getName()
                        + "must be annotated by @Id or @EmbeddedId."));
        boolean isGeneratedPk = MetaDataParser.isGeneratedPk(pkField);
        switch (primaryKeyType) {
            case SINGLE:
                return new SinglePrimaryKey(getPrimaryKeyInternalMetaField(pkField, metaData), metaData, pkField, isGeneratedPk);
            case COMPLEX:
                Class<?> embeddedFieldClass = pkField.getType();
                EmbeddableMetaData embeddableMetaData = embeddedEntityConfig.get(embeddedFieldClass);
                Objects.requireNonNull(embeddableMetaData, "Embeddable type must be initialized!");
                List<InternalMetaField> primaryKeys = embeddableMetaData
                        .getFields().stream()
                        .map(field -> getPrimaryKeyInternalMetaField(field, metaData))
                        .collect(Collectors.toList());
                return new EmbeddedPrimaryKey(embeddableMetaData, primaryKeys, metaData, pkField, isGeneratedPk);
            default:
                throw new OrmException("AbstractMetaPrimaryKey should have a SINGLE or COMPLEX type.");
        }
    }

    private InternalMetaField getPrimaryKeyInternalMetaField(Field field, MetaData metaData) {
        Class<?> fieldType = field.getType();
        MappingType mappingType = MappingType.getMappingType(fieldType);
        String fieldName = field.getName();
        Optional<String> dbColumnName = getDbColumnName(field);
        return new InternalMetaField.Builder(field, metaData)
                .fieldType(fieldType)
                .mappingType(mappingType)
                .fieldName(fieldName)
                .dbFieldName(dbColumnName.orElse(fieldName.toLowerCase()))
                .setPrimaryKey(true)
                .build();
    }

    /**
     * Factory method
     *
     * @param field
     * @param metaData
     * @return object of Internal, External or Collection + MetaField
     */
    private AbstractMetaField initMetaField(Field field, MetaData metaData) {
        Class<?> fieldType = field.getType();
        MappingType mappingType = MappingType.getMappingType(fieldType);
        String fieldName = field.getName();
        Optional<FetchType> fetchType = getFetchTypeValue(field);

        switch (mappingType.getFieldType()) {
            case INTERNAL:
                Optional<String> dbColumnName = getDbColumnName(field);
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
                ExternalMetaField.Builder externalFieldBuilder = new ExternalMetaField.Builder(field, metaData);
                if (hasJoinColumnAnnotation(field)) {
                    String joinColumnName = getJoinColumnName(field)
                            .orElseThrow(() -> new OrmException("@JoinColumn must be initialized. Field: " + fieldName));
                    externalFieldBuilder.foreignKeyFieldName(joinColumnName);
                } else if (hasPrimaryKeyJoinColumnAnnotation(field)) {
                    String primaryKeyJoinColumnName = getPrimaryKeyJoinColumnName(field)
                            .orElseThrow(() -> new OrmException("@PrimaryKeyJoinColumn must be initialized. Field: " + fieldName));
                    externalFieldBuilder.foreignKeyFieldName(primaryKeyJoinColumnName);
                } else if (hasMapsIdAnnotation(field)) {
                    String mapsIdColumnName = getMapsIdColumnName(field)
                            .orElseThrow(() -> new OrmException("@MapsId must be initialized. Field: " + fieldName));
                    externalFieldBuilder.foreignKeyFieldName(mapsIdColumnName);
                }

                return externalFieldBuilder
                        .fieldType(fieldType)
                        .mappingType(mappingType)
                        .fieldName(fieldName)
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
