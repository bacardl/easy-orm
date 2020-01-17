package com.softserve.easy.jdbc;

import com.softserve.easy.action.ActionQueue;

import java.sql.Connection;

public class JDBCPersister implements Persister{
    private ActionQueue actionQueue;
    private Connection connection;

}
