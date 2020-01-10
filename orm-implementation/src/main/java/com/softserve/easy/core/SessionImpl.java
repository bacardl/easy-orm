package com.softserve.easy.core;

import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SessionImpl implements Session {
    private Connection connection;
    private  Map<Class<?>, MetaData> metaDataMap;
    private Transaction transaction;
    private DependencyGraph dependencyGraph;

    public SessionImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Serializable save(Object object) {
        return null;
    }

    @Override
    public <T> T get(Class<T> entityType, Serializable id) {
        return null;
    }

    @Override
    public void update(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Object object) {
      Stack<String> deleteQueries = new Stack<>();
      Set<Class<?>> traversedClasses = new HashSet<>();
      recursiveDelete(deleteQueries,traversedClasses,object);
      while (!deleteQueries.isEmpty()){
          try {
              Statement statement = connection.createStatement();
              statement.executeUpdate(deleteQueries.pop());
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
    }

    private void recursiveDelete(Stack<String> query, Set<Class<?>> traversedClasses, Object object) {
        Class<?> currentClass = object.getClass();
        List<ExternalMetaField> externalFields = metaDataMap.get(object.getClass()).getExternalMetaField();
        try {
            query.push("DELETE FROM " + metaDataMap.get(currentClass).getEntityDbTableName()
                    + " WHERE " + metaDataMap.get(currentClass).getPrimaryKey()
                    + " = " + metaDataMap.get(currentClass).getPrimaryKey().get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        traversedClasses.add(currentClass);
        if (externalFields.size() != 0) {
            for (ExternalMetaField externalField : externalFields) {
                if (!traversedClasses.contains(externalField.getFieldType())) {
                    Object externalObject = null;
                    try {
                        externalObject = currentClass.getDeclaredField(externalField.getFieldName()).get(object);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    recursiveDelete(query, traversedClasses, externalObject);
                }
            }
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transaction beginTransaction() {
        throw new UnsupportedOperationException();
    }
}
