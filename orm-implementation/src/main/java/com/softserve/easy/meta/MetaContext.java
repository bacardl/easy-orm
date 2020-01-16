package com.softserve.easy.meta;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MetaContext {
    final private Map<Class<?>, MetaData> metaDataMap;
    final private DependencyGraph dependencyGraph;
    final private Properties properties;
    final private Set<Class<?>> observedClasses;

    public MetaContext(
            Map<Class<?>, MetaData> metaDataMap,
            DependencyGraph dependencyGraph,
            Properties properties,
            Set<Class<?>> observedClasses) {
        this.metaDataMap = metaDataMap;
        this.dependencyGraph = dependencyGraph;
        this.properties = properties;
        this.observedClasses = observedClasses;
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
}
