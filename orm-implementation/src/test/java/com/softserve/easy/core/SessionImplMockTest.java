package com.softserve.easy.core;

import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.Map;

class SessionImplMockTest {

    @InjectMocks
    private Session session;
    @Mock
    private Connection connection;
    @Mock
    private Map<Class<?>, MetaData> metaDataMap;
    @Mock
    private DependencyGraph dependencyGraph;

    @Before
    public void setUp() {
        session = new SessionImpl(connection, metaDataMap, dependencyGraph);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void name() {
    }
}