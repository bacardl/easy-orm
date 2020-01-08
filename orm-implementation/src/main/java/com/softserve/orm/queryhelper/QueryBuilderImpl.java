package com.softserve.orm.queryhelper;

import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilderImpl implements QueryBuilder {
    private String tableName;
    private StringBuilder query;

    public QueryBuilderImpl(String tableName) {
        this.query = new StringBuilder();
        this.tableName = tableName;
    }

    @Override
    public String prepareSelectQuery() {
        return "SELECT * FROM " + tableName + query + ";";
    }

    @Override
    public String prepareUpdateQuery(List<String> columnNames, List<Object> values) {
        String columns = String.join(",", columnNames);
        String vals = values.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("', '","'","'"));
        return "UPDATE " + tableName + " SET (" + columns + ") = (" + vals + ")" + query + ";";
    }

    @Override
    public String prepareDeleteQuery() {
        return "DELETE FROM " + tableName + query;
    }

    @Override
    public String prepareInsertQuery(List<String> columnNames, List<Object> values) {
        String columns = String.join(",", columnNames);
        String vals = values.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",","'","'"));;
        query.setLength(0);
        query.append("INSERT INTO ").append(tableName).append(" (");
        query.append(columns).append(") VALUES(").append(vals).append(");");
        return query.toString();
    }

    public QueryBuilderImpl where() {
        query.append(" WHERE");
        return this;
    }

    public QueryBuilderImpl like(LikeType likeType, Object requiredObj, String columnName) {
        query.append("'").append(columnName).append("'").append(" LIKE ");
        if (likeType == LikeType.STARTS_WITH) {
            query.append("'").append(requiredObj).append("% ").append("'");
        }
        if (likeType == LikeType.CONTAINS) {
            query.append("'").append("%").append(requiredObj).append("% ").append("'");
        }
        if (likeType == LikeType.ENDS_WITH) {
            query.append("'").append("%").append(requiredObj).append("'");
        }

        return this;
    }

    public QueryBuilderImpl and() {
        query.append(" AND");
        return this;
    }

    public QueryBuilderImpl or() {
        query.append(" OR");
        return this;
    }

    public QueryBuilderImpl eqls(String columnName, Object requiredOdj) {
        query.append(" `").append(columnName).append("`=").append(requiredOdj);
        return this;
    }


    public QueryBuilderImpl limit(int amount) {
        query.append(" LIMIT ").append(amount);
        return this;
    }

    public QueryBuilderImpl order(String column, boolean desc) {
        query.append(" ORDER BY `").append(column).append("`").append(desc ? " DESC" : "");
        return this;
    }

}

