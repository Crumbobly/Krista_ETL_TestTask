package ru.lab.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.utils.FileUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseWorker.class.getName());

    private String url;
    private String username;
    private String password;

    public DatabaseWorker(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Выполнение простого скрипта, не возвращающего ответ.
     *
     * @param scriptPath - путь до скрипта
     */
    public void executeScript(String scriptPath){
        try (final Connection connection = getConnection()) {
            final Statement statement = connection.createStatement();
            final String sql = FileUtils.readFile(scriptPath);
            LOGGER.info("Выполнение скрипта {}", scriptPath);
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
