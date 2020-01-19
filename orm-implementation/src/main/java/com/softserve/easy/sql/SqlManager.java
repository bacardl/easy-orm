package com.softserve.easy.sql;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.*;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class SqlManager {
    private static final Logger LOG = LoggerFactory.getLogger(SqlManager.class);
    private final MetaContext metaContext;

    public SqlManager(MetaContext metaContext) {
        this.metaContext = metaContext;
    }

    public SelectQuery buildSelectByIdSqlQuery(Class<?> entity, Serializable id) {
        SelectQuery selectQuery = new SelectQuery();
        MetaData rootMetaData = metaContext.getMetaDataMap().get(entity);

        List<InternalMetaField> internalMetaFields = rootMetaData.getInternalMetaField();
        for (InternalMetaField metaField : internalMetaFields) {
            selectQuery.addAliasedColumn(metaField.getInternalDbColumn(), metaField.getDbFieldFullName());
        }

        List<ExternalMetaField> externalMetaFields = rootMetaData.getExternalMetaField();
        for (ExternalMetaField metaField : externalMetaFields) {
            selectQuery.addAliasedColumn(metaField.getExternalDbColumn(), metaField.getForeignKeyFieldFullName());
            addDependencyToSqlQuery(selectQuery, metaField);
        }

        selectQuery.addCondition(BinaryCondition.equalTo(rootMetaData.getPkMetaField().getInternalDbColumn(), id));
        LOG.info("Built select query for {} entity with id: {}.\n{}",entity.getSimpleName(), id, selectQuery.toString());
        return selectQuery;
    }

    private void addDependencyToSqlQuery(SelectQuery selectQuery, ExternalMetaField metaField) {
        MetaData parentMetaData = metaField.getMetaData();
        MetaData childMetaData = metaContext.getMetaDataMap().get(metaField.getFieldType());

        List<InternalMetaField> internalMetaFields = childMetaData.getInternalMetaField();
        for (InternalMetaField internalMetaField : internalMetaFields) {
            selectQuery.addAliasedColumn(internalMetaField.getInternalDbColumn(), internalMetaField.getDbFieldFullName());
        }

        List<ExternalMetaField> externalMetaFields = childMetaData.getExternalMetaField();
        for (ExternalMetaField externalMetaField : externalMetaFields) {
            selectQuery.addAliasedColumn(externalMetaField.getExternalDbColumn(), externalMetaField.getForeignKeyFieldFullName());
        }

        DbJoin dbJoin = new DbJoin(parentMetaData.getDbTable().getSpec(),
                parentMetaData.getDbTable(),
                childMetaData.getDbTable(),
                new DbColumn[]{metaField.getExternalDbColumn()},
                new DbColumn[]{childMetaData.getPkMetaField().getInternalDbColumn()});

        selectQuery.addJoins(SelectQuery.JoinType.INNER, dbJoin);
    }

    public static String buildSelectSqlQuery() {
        DbSpec spec = new DbSpec();
        DbSchema schema = spec.addSchema("simple");

        DbTable usersTable = schema.addTable("users");
        DbColumn idCol = usersTable.addColumn("id", "bigint", null);
        DbColumn usernameCol = usersTable.addColumn("username", "varchar", 255);
        DbColumn passwordCol = usersTable.addColumn("password", "varchar", 255);
        DbColumn countryCol = usersTable.addColumn("country_code", "integer", null);
        usersTable.primaryKey("users_pk", "id");


        DbTable countryTable = schema.addTable("countries");
        DbColumn codeCol = countryTable.addColumn("code", "integer", null);
        DbColumn nameCol = countryTable.addColumn("name", "varchar", 255);
        countryTable.primaryKey("countries_pk", "code");


        DbJoin dbJoin = new DbJoin(spec, usersTable, countryTable,
                new DbColumn[]{countryCol}, new DbColumn[]{codeCol});
        SelectQuery selectQuery = new SelectQuery()
                .addAllTableColumns(usersTable)
                .addAllTableColumns(countryTable)
                .addJoins(SelectQuery.JoinType.INNER, dbJoin)
                .addCondition(BinaryCondition.equalTo(idCol, 2))
                .validate();
        InsertQuery insertQuery = new InsertQuery(usersTable)
                .addColumn(usernameCol, "test")
                .addColumn(passwordCol, "pass")
                .addColumn(countryCol, 100);
        return selectQuery.toString();
    }

}
