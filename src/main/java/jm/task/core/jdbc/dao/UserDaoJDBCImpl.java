package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl extends Util implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());
    private static final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sqlQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS users (").
                append("id BIGINT NOT NULL AUTO_INCREMENT, ")
                .append("name VARCHAR(45) NULL, ")
                .append("lastname VARCHAR(45) NULL, ")
                .append("age TINYINT NULL, ")
                .append("PRIMARY KEY (id), ")
                .append("UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE)")
                .toString();

        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
            LOGGER.log(Level.INFO, "Таблица создана");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "При создании таблицы произошла ошибка");
        }
    }

    public void dropUsersTable() {
        String sqlQuery = "DROP TABLE IF EXISTS users";

        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlQuery);
            LOGGER.log(Level.INFO, "Таблица была удаленна");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "При удалении таблицы произошла ошибка");
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlQuery = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            LOGGER.log(Level.INFO, "В таблицу добавлена новая запись");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "При добавлении записи произошла ошибка");
        }
    }

    public void removeUserById(long id) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            LOGGER.log(Level.INFO, "В таблице удалена запись с индексом " + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "При удалении записи с индексом " + id + " произошла ошибка");
        }
    }

    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        List<User> list = new ArrayList<>();

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));

                list.add(user);
            }
            LOGGER.log(Level.INFO, "Из таблицы успешно получены все данные");
            System.out.println(list);
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "При получении всех данных из таблицы произошла ошибка");
            return null;
        }
    }

    public void cleanUsersTable() {
        String sqlQuery = "TRUNCATE TABLE users";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQuery);
            LOGGER.log(Level.INFO, "Таблица очищена");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Таблица не очищена");
        }
    }

}
