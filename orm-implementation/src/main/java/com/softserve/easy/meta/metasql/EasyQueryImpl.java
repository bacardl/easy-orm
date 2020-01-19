package com.softserve.easy.meta.metasql;

import com.softserve.easy.exception.OrmException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyQueryImpl implements EasyQuery{
    private String userQuery;
    private StringBuilder query;

    public EasyQueryImpl(String query) {
        this.userQuery = query;
        this.query = new StringBuilder();
    }

    private void validate(){
        if(matchesDelete()|| matchesInsert()|| matchesSelect()||matchesUpdate()){

        } else {
            throw new OrmException("query type not specified : " + userQuery);
        }
    }

    @Override
    public int executeUpdate() {
        return 0;
    }

    @Override
    public List<?> list() {
        return null;
    }

    @Override
    public ObjectQuery setMaxResult(int rowNumber) {
        return null;
    }

    @Override
    public ObjectQuery setParameter(String parameterName, Object value) {
        return null;
    }

    @Override
    public ObjectQuery setParameter(int position, Object value) {
        return null;
    }

    private String prepareInsertQuery(){
//        String insertQ = new InsertQuery();
        return null;
    }

    private String prepareDeleteQuery(){
        return null;
    }

    private String prepareUpdateQuery(){
        return null;
    }

    private String prepareSelectQuery(){
        return null;
    }

    public boolean matchesSelect(){
        Pattern pattern = Pattern.compile("(select )?from \\w+( where (\\w+(\\s)*=(\\s)*(:)?\\w+(,)*(\\s)*)+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();
    }

    public boolean matchesDelete(){
        Pattern pattern = Pattern.compile("delete (from )?\\w+( where (\\w+(\\s)*=(\\s)*:\\w+(,)*(\\s)*)+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();
    }

    public boolean matchesUpdate(){
        Pattern pattern = Pattern.compile("update \\w+ set (\\w+\\s*=\\s*:\\w+,*(\\s)*)+((\\s)*(where\\s(\\w+(\\s)*=(\\s)*:(\\s)*\\w+(,)*(\\s)*)+))*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();

    }

    public boolean matchesInsert(){
        Pattern pattern = Pattern.compile("insert into \\w+(\\s)*\\((\\w+(,)*(\\s)*)*\\) select (\\w+(,)*(\\s)*)* from \\w+( where\\s(\\w+(\\s)*=(\\s)*:(\\s)*\\w+(,)*(\\s)*)+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();

    }



}
