package com.softserve.orm.cfg;

import com.softserve.orm.helper.LoadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {
    private static final Logger LOG = LoggerFactory.getLogger(Environment.class);
    private static final Properties GLOBAL_PROPERTIES;

    static {
        GLOBAL_PROPERTIES = new Properties();
        try (InputStream stream = LoadHelper.getResourceAsStream("orm.properties")) {
            GLOBAL_PROPERTIES.load(stream);
            LOG.info("orm.properties has been loaded successfully.");
        } catch (Exception he) {
            LOG.error("Couldn't load orm.properties.");
        }
    }

    public static Properties getProperties() {
        Properties copy = new Properties();
        copy.putAll(GLOBAL_PROPERTIES);
        return copy;
    }
}
