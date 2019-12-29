package com.softserve.easy.cfg;

import com.softserve.easy.helper.LoadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Environment {
    private static final Logger LOG = LoggerFactory.getLogger(Environment.class);
    private static final Properties GLOBAL_PROPERTIES;

    private static final String ORM_PROPERTIES = "easy.properties";

    static {
        GLOBAL_PROPERTIES = new Properties();
        try (InputStream stream = LoadHelper.getResourceAsStream(ORM_PROPERTIES)) {
            GLOBAL_PROPERTIES.load(stream);
            LOG.info("easy.properties has been loaded successfully.");
        } catch (Exception he) {
            LOG.error("Couldn't load easy.properties.");
        }
    }

    public static Properties getProperties() {
        Properties copy = new Properties();
        copy.putAll(GLOBAL_PROPERTIES);
        return copy;
    }
}
