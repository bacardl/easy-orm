package com.softserve.easy.helper;

import com.softserve.easy.exception.OrmException;

import java.io.InputStream;
import java.util.Objects;

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
