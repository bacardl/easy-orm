package com.softserve.easy.client.dao;

import com.github.database.rider.core.DBUnitRule;
import com.softserve.easy.cfg.Configuration;
import org.junit.Rule;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DbUnitTest {
    private Configuration configuration;

    public DbUnitTest() {
        this.configuration = new Configuration();
    }

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance();

    public Connection getClientConnection() throws SQLException {
        return dbUnitRule.getDataSetExecutor().getRiderDataSource().getConnection();
    }
    public Configuration getConfiguration() {
        return this.configuration;
    }
}
