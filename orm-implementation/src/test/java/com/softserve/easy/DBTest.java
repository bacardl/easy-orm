package com.softserve.easy;


import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.softserve.easy.jdbc.ConnectionFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.*;

@RunWith(JUnit4.class)
@DataSet(value = "dataset/yml/user/user.yml", executeScriptsBefore = {"dataset/schema.sql"})
public class DBTest {
    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance();
    @Test
    @ExpectedDataSet(value = "dataset/yml/user/user-create.yml")
    public void createUser() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = connectionFactory.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER (NAME) VALUES (?)")) {
            preparedStatement.setString(1,"@test");
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USER");
            while (resultSet.next()) {
                long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                System.out.println("id:" + id + " name: " + name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}