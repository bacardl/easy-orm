package mapper;

import annotation.*;
import models.Auto;
import models.User;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoGetterImpl implements InfoGetter{
    Object currentClass;
    Metadata metadata = new Metadata();

    public InfoGetterImpl() {
    }

    private void getTableName() throws NullPointerException{
        String tableName = currentClass.getClass().getAnnotation(Table.class).name();
        metadata.tableName = tableName;
    }

    @Override
    public void getColumns() {

    }

    @Override
    public void getPrimaryKeys() {

    }

    @Override
    public void getForeignKeys() {

    }

    private void getColumnsAndPrimaryKeysAndForeignKeys() throws IllegalAccessException{
        String columnName = "";
        Object columnValue = null;
        for(Field field : currentClass.getClass().getDeclaredFields()) {
            boolean wasException = false;
            field.setAccessible(true);

            if( null != field.getAnnotation(OneToMany.class)) {
                Map<String, Object> map = new HashMap<>();
                for(Field f : field.get(currentClass).getClass().getDeclaredFields()) {
                    if(null != field.getAnnotation(GeneratedValue.class)) {
                        map.put(field.getName(), field.get(currentClass));
                        continue;
                    }
                }
                metadata.foreignKeys.put(field.getName(), map);
                continue;
            }

            if( null != field.getAnnotation(GeneratedValue.class)) {
                metadata.primaryKeys.put(field.getName(), field.get(currentClass));
                continue;
            }

            //take Annotation name or take variables name
            try {
                columnName = field.getAnnotation(Column.class).name();
            } catch (NullPointerException e) {
                wasException = true;
            }
            if (wasException) {
                columnName = field.getName();
            }

            columnValue = field.get(currentClass);
            metadata.columns.put(columnName, columnValue);
        }
    }

    public Metadata getClassInfo(Object o) throws IllegalAccessException{
        currentClass = o;
        getTableName();
        getColumnsAndPrimaryKeysAndForeignKeys();
        return metadata;
    }

    public static void main(String[] args) {
        InfoGetterImpl infoGetter = new InfoGetterImpl();
        User user = new User("Jony", 22);
        Auto auto = new Auto("Q1", "red");
        Auto auto1 = new Auto("Q2", "red");
        user.addAuto(auto);
        user.addAuto(auto1);
        List<Object> list = new ArrayList<>();
        try {
            System.out.println(infoGetter.getClassInfo(user));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
