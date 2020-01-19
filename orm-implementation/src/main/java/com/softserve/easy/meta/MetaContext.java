package com.softserve.easy.meta;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MetaContext {
    final private Map<Class<?>, MetaData> metaDataMap;
    final private DependencyGraph dependencyGraph;
    final private Properties properties;
    final private Set<Class<?>> observedClasses;

    private final DbSpec dbLogicalSpecification;
    private final DbSchema dbSchema;

    public MetaContext(
            Map<Class<?>, MetaData> metaDataMap,
            DependencyGraph dependencyGraph,
            Properties properties,
            Set<Class<?>> observedClasses,
            DbSpec dbLogicalSpecification,
            DbSchema dbSchema) {
        this.metaDataMap = metaDataMap;
        this.dependencyGraph = dependencyGraph;
        this.properties = properties;
        this.observedClasses = observedClasses;
        this.dbLogicalSpecification = dbLogicalSpecification;
        this.dbSchema = dbSchema;
    }

    public Map<Class<?>, MetaData> getMetaDataMap() {
        return metaDataMap;
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }
    public Properties getProperties() {
        return properties;
    }
    public Set<Class<?>> getObservedClasses() {
        return observedClasses;
    }
    public DbSpec getDbLogicalSpecification() {
        return dbLogicalSpecification;
    }
    public DbSchema getDbSchema() {
        return dbSchema;
    }
}
