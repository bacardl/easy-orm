package com.softserve.easy;


import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
//import com.softserve.easy.jdbc.ConnectionFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/complex/yml/user/user.yml",
        strategy = SeedStrategy.CLEAN_INSERT,
        executeScriptsBefore = {"dataset/complex/db-schema.sql"})
public class DBTest {
    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance();




//    private static Session session;
//
//    @BeforeClass
//    public static void init() {
//        Configuration configuration = initTestConfiguration();
//        SessionFactory sessionFactory = configuration.buildSessionFactory();
//        session = sessionFactory.openSession();
//    }
//
//    private static Configuration initTestConfiguration() {
//        throw new UnsupportedOperationException();
//    }

    @Test
//    @ExpectedDataSet(value = "dataset/yml/user/user-create.yml")
    public void createUserUsingJdbc() {
//        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
//        Connection connection = connectionFactory.getConnection();
//        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER (NAME) VALUES (?)")) {
//            preparedStatement.setString(1, "@test");
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

//        try (Statement statement = connection.createStatement()) {
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");
//            while (resultSet.next()) {
//                long id = resultSet.getLong("PERSON_ID");
//                String username = resultSet.getString("USERNAME");
//                String password = resultSet.getString("PASSWORD");
//                String email = resultSet.getString("EMAIL");
//                int countryCode = resultSet.getInt("COUNTRY_CODE");
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder
//                        .append("id: ").append(id).append("\n")
//                        .append("username: ").append(username).append("\n")
//                        .append("password: ").append(password).append("\n")
//                        .append("email: ").append(email).append("\n")
//                        .append("country_code: ").append(countryCode).append("\n");
//
//                System.out.println(stringBuilder.toString());
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }
//
//    @Test
//    @ExpectedDataSet(value = "dataset/yml/user/user-create.yml")
//    public void shouldCreateUserWithParameter() {
//        session.save(new User("@test", 12));
//    }
//
//    @Test
//    public void shouldReadUserByID() {
//        User user = session.get(User.class, 1L);
//        assertThat(user).isNotNull();
//        assertThat(user.getId()).isEqualTo(1);
//    }
//
//    @Test
//    @ExpectedDataSet(value = "dataset/yml/user/user-update.yml")
//    public void updateUser() {
//        User user = new User(1, "@realpestano", 1);
//        session.update(user);
//    }
//
//    @Test
//    @ExpectedDataSet(value = "dataset/yml/user/user-delete.yml")
//    public void deleteUser() {
//        session.delete(new User());
//    }
}