package com.softserve.easy.core;

import com.softserve.easy.meta.MetaContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

class SessionMockTest {

    @InjectMocks
    private Session session;
    @Mock
    private Connection connection;
    @Mock
    private MetaContext metaContext;


    @Before
    public void setUp() {
        session = new SessionImpl(connection, metaContext);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void name() {
    }
}