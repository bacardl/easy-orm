package com.softserve.easy.sql;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.simpleEntity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SqlManagerTest {
    private static SqlManager sqlManager;

    @BeforeAll
    static void initSqlManager(){
        MetaContext metaContext = new Configuration().getMetaContext();
        sqlManager = new SqlManager(metaContext);
    }
    @Test
    void buildSelectSqlQueryById() {
        System.out.println(sqlManager.buildSelectByIdSqlQuery(User.class, 2L));
    }

    @Test
    void buildSelectSqlQuery() {
        System.out.println(SqlManager.buildSelectSqlQuery());
    }
}