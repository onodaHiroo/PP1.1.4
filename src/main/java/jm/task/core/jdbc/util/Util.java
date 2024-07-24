package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/pp_1_1_3-4_jdbc_hibernate";
    private static final String USER = "root";
    private static final String PASSWORD = "rootlocalhost123";
    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Class.forName(DRIVER);
            LOGGER.log(Level.FINE, "Подключение к базе данных произошло успешно.");
        } catch (ClassNotFoundException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "При подключении к базе данных произошла ошибка.", ex);
        }
        return connection;
    }
}
