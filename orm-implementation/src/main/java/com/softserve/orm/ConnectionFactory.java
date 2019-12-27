package com.softserve.orm;

import com.softserve.orm.cfg.Environment;
import com.softserve.orm.exception.OrmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

import static com.softserve.orm.cfg.ConfigPropertyConstant.*;

/**----SINGLETON-----*/
public class ConnectionFactory {
    private static Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);
    private static ConnectionFactory INSTANCE;

    private String driverClassName;
    private String username;
    private String password;
    private String url;


    public static ConnectionFactory getInstance() {
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new ConnectionFactory();
        }
        return INSTANCE;
    }

    private ConnectionFactory() {
        initProperties();
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            LOG.error("A driver class hasn't been loaded: {}", driverClassName);
            throw new OrmException("Cannot load a driver class: " + e.getMessage());
        }
    }

    private void initProperties() {
        Properties ormProperties = Environment.getProperties();
        driverClassName = ormProperties.getProperty(DRIVER_CLASS_PROPERTY);
        username = ormProperties.getProperty(USERNAME_PROPERTY);
        password = ormProperties.getProperty(PASSWORD_PROPERTY);
        url = ormProperties.getProperty(URL_PROPERTY);

    }

    public Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            LOG.error("Cannot establish connection to the database {}", e.getMessage());
            throw new OrmException("Couldn't connect to database.", e);
        }
        return connection;
    }
}
