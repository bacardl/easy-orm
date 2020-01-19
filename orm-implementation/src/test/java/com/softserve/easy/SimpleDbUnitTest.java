package com.softserve.easy;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.softserve.easy.constant.ConfigPropertyConstant.ENTITY_PACKAGE_PROPERTY;

@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/simple/yml/data.yml",
        strategy = SeedStrategy.INSERT, cleanAfter = true,
        executeScriptsBefore = {"dataset/simple/db-schema.sql"},
        executeScriptsAfter = {"dataset/simple/drop-db-schema.sql"})
public abstract class SimpleDbUnitTest extends DbUnitTest {

    private static final String SIMPLE_ENTITY_PACKAGE = "com.softserve.easy.entity.simple";

    public SimpleDbUnitTest() {
        getConfiguration().setProperty(ENTITY_PACKAGE_PROPERTY, SIMPLE_ENTITY_PACKAGE);
    }
}
