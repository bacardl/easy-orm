package com.softserve.orm.helper;

import com.softserve.orm.exception.OrmException;

import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;

public class LoadHelper {
    public static InputStream getResourceAsStream(String resource) {
        InputStream stream = null;
        stream = LoadHelper.class.getResourceAsStream(resource);
        if (Objects.isNull(stream))
            stream = LoadHelper.class.getClassLoader().getResourceAsStream(resource);
        if (Objects.isNull(stream))
            throw new OrmException(resource + " not found.");
        return stream;
    }
}
