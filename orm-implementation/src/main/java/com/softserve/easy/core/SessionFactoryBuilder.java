package com.softserve.easy.core;

import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;

import javax.sql.DataSource;
import java.util.Map;

public class SessionFactoryBuilder {
    private DataSource dataSource;
    private Map<Class<?>, MetaData> metaDataMap;
    private DependencyGraph dependencyGraph;

    public SessionFactoryBuilder() {
    }

    public SessionFactoryBuilder setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public SessionFactoryBuilder setMetaDataMap(Map<Class<?>, MetaData> metaDataMap) {
        this.metaDataMap = metaDataMap;
        return this;
    }

    public SessionFactoryBuilder setDependencyGraph(DependencyGraph dependencyGraph) {
        this.dependencyGraph = dependencyGraph;
        return this;

    }

    public SessionFactory build() {
        return new SessionFactoryImpl(dataSource, metaDataMap, dependencyGraph);
    }
}
