package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());
    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            String sqlQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS users (").
                    append("id BIGINT NOT NULL AUTO_INCREMENT, ")
                    .append("name VARCHAR(45) NULL, ")
                    .append("lastname VARCHAR(45) NULL, ")
                    .append("age TINYINT NULL, ")
                    .append("PRIMARY KEY (id), ")
                    .append("UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE)")
                    .toString();

            transaction = session.beginTransaction();

            session.createSQLQuery(sqlQuery).addEntity(User.class).executeUpdate();
            transaction.commit();
            LOGGER.log(Level.INFO, "Таблица создана");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "При создании таблицы произошла ошибка");
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            String sqlQuery = "DROP TABLE IF EXISTS users";

            transaction = session.beginTransaction();

            session.createSQLQuery(sqlQuery).addEntity(User.class).executeUpdate();
            transaction.commit();
            LOGGER.log(Level.INFO, "Таблица удалена");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "При удалении таблицы произошла ошибка");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            LOGGER.log(Level.INFO, "В таблицу добавлена новая запись");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "При добавлении записи произошла ошибка");
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
            LOGGER.log(Level.INFO, "В таблице удалена запись с индексом " + id);
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "При удалении записи с индексом " + id + " произошла ошибка");
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> users = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            users = session.createCriteria(User.class).list();
            transaction.commit();
            LOGGER.log(Level.INFO, "Из таблицы успешно получены все данные");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "При получении всех данных из таблицы произошла ошибка");
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        String sqlQuery = "TRUNCATE TABLE users";

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(sqlQuery).executeUpdate();
            transaction.commit();
            LOGGER.log(Level.INFO, "Таблица очищена");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Таблица не очищена");
        }
    }
}
