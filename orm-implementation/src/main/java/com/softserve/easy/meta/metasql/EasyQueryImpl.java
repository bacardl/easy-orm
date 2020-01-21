package com.softserve.easy.meta.metasql;

import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.softserve.easy.exception.OrmException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EasyQueryImpl implements EasyQuery {
    private String userQuery;
    private StringBuilder query;
    private QueryType queryType;

    public EasyQueryImpl(String query) {
        this.userQuery = query;
        this.query = new StringBuilder();
    }

    private void validate() {
        if (matchesDelete()) {
            this.queryType = QueryType.DELETE;
        } else if (matchesSelect()) {
            this.queryType = QueryType.SELECT;
        } else if (matchesUpdate()) {
            this.queryType = QueryType.UPDATE;
        } else if (matchesFrom()) {
            this.queryType = QueryType.FROM;
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
        int index = query.indexOf(":" + parameterName);
        query.replace(index, index + parameterName.length(), value.toString());
        return this;
    }

    @Override
    public ObjectQuery setParameter(int position, Object value) {
        return null;
    }

    private String prepareInsertQuery() {
//        String insertQ = new InsertQuery();
        return null;
    }

    private String prepareDeleteQuery() {
        return null;
    }

    private String prepareUpdateQuery() {
        return null;
    }

    private String prepareSelectQuery() {
        return null;
    }

    public boolean matchesSelect() {
        Pattern pattern = Pattern.compile("select .+ from \\w+( where (\\w+(\\s)*=(\\s)*(:)?\\w+(,)*(\\s)*)+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();
    }

    public boolean matchesFrom() {
        Pattern pattern = Pattern.compile("^from \\w+( where (\\w+(\\s)*=(\\s)*(:)?\\w+(,)*(\\s)*)+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();
    }

    public boolean matchesDelete() {
        Pattern pattern = Pattern.compile("delete (from )?\\w+( where (\\w+(\\s)*=(\\s)*:\\w+(,)*(\\s)*)+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();
    }

    public boolean matchesUpdate() {
        Pattern pattern = Pattern.compile("update \\w+ set (\\w+\\s*=\\s*:\\w+,*(\\s)*)+((\\s)*(where\\s(\\w+(\\s)*=(\\s)*:(\\s)*\\w+(,)*(\\s)*)+))*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        return matcher.matches();

    }

//    public boolean matchesInsert() {
//        Pattern pattern = Pattern.compile("insert into \\w+(\\s)*\\((\\w+(,)*(\\s)*)*\\) select (\\w+(,)*(\\s)*)* from \\w+( where\\s(\\w+(\\s)*=(\\s)*:(\\s)*\\w+(,)*(\\s)*)+)?", Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(userQuery);
//        return matcher.matches();
//
//    }

    public String extractClassName() {
        Pattern pattern;
        this.validate();
        if (queryType.equals(QueryType.FROM)) {
            pattern = Pattern.compile("^\\s*from\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        } else if (queryType.equals(QueryType.DELETE)) {
            pattern = Pattern.compile("delete(?:\\sfrom)*\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        } else if (queryType.equals(QueryType.UPDATE)) {
            pattern = Pattern.compile("update\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile("select.+from\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(userQuery);
        String tableName;
        boolean find = matcher.find();
        if (find) {
            tableName = matcher.group(1);
        } else {
            throw new OrmException("table name not found");
        }
        return tableName;
    }

    public String extractWhereClause() {
        Pattern pattern = Pattern.compile("where ((?!and$|or$).*)",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);
        String match = "";
        if (matcher.find()) {
             match = matcher.group(1);
        }
        return match.trim();
    }

    public List<String> extractFieldNamesAfterWhereClause(String extractedPart){
        List<String> temp = Arrays.asList(extractedPart.split("\\s*=\\s*:?\\w+\\s*|\\s*AND\\s*|\\s*OR\\s*|\\s*and\\s*|\\s*or\\s*"));
        ArrayList<String> fieldNames = new ArrayList<>(temp);
        //delete possible empty strings
        fieldNames.removeIf(String::isEmpty);
        return fieldNames;
    }

    public boolean containsWhereClause(){
        Pattern pattern = Pattern.compile(".+where .+",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userQuery);

        return matcher.matches();
    }



}
