package com.softserve.easy;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.softserve.easy.constant.ConfigPropertyConstant.ENTITY_PACKAGE_PROPERTY;

@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/complex/yml/user/user.yml",
        strategy = SeedStrategy.INSERT, cleanAfter = true,
        executeScriptsBefore = {"dataset/complex/db-schema.sql"},
        executeScriptsAfter = {"dataset/complex/drop-db-schema.sql"})
public abstract class ComplexDbUnitTest extends DbUnitTest {
    private static final String COMPLEX_ENTITY_PACKAGE = "com.softserve.easy.entity.complex";

    public ComplexDbUnitTest() {
        getConfiguration().setProperty(ENTITY_PACKAGE_PROPERTY,COMPLEX_ENTITY_PACKAGE);
    }
}
