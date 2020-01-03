package com.softserve.orm.queryhelper;

public class QueryBuilderImpl implements QueryBuilder {
    private String tableName;
    private StringBuilder query;

    public QueryBuilderImpl(String tableName) {
        this.query = new StringBuilder();
        this.tableName = tableName;
    }

    @Override
    public String prepareSelectQuery() {
        return "SELECT * FROM " + tableName + query;
    }

    @Override
    public String prepareUpdateQuery() {
        return null;
    }

    @Override
    public String prepareDeleteQuery() {
        return null;
    }

    @Override
    public QueryBuilder where() {
        query.append(" WHERE");
        return this;
    }

    @Override
    public QueryBuilder like(LikeType likeType, Object requiredObj, String columnName) {
        query.append("'").append(columnName).append("'").append(" LIKE ");
        if (likeType == LikeType.STARTS_WITH) {
            query.append(requiredObj).append("% ");
        }
        if (likeType == LikeType.CONTAINS) {
            query.append("%").append(requiredObj).append("% ");
        }
        if (likeType == LikeType.ENDS_WITH) {
            query.append("%").append(requiredObj);
        }

        return this;
    }


    @Override
    public QueryBuilder and() {
        query.append(" AND");
        return this;
    }

    @Override
    public QueryBuilder or() {
        query.append(" OR");
        return this;
    }

    @Override
    public QueryBuilder eqls(String columnName, Object requiredOdj) {
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

