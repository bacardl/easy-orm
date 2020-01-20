package com.softserve.easy.bind;

import java.sql.ResultSet;
import java.util.Optional;

public interface EntityBinder {
    <T> Optional<T> buildEntity(Class<T> clazz, ResultSet resultSet) throws Exception;

    <T> T buildLazyEntity(Class<T> entityType, ResultSet resultSet) throws Exception;
}
