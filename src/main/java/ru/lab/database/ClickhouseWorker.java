package ru.lab.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.ClickhouseConfig;
import ru.lab.utils.FileUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ClickhouseWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClickhouseWorker.class);

    private final static String initScriptPath = "db/clickhouse/init.sql";
    private final static String createScriptPath = "db/clickhouse/create_db.sql";


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(ClickhouseConfig.URL_DB, ClickhouseConfig.USER, ClickhouseConfig.PASSWORD);
    }

    public void crateDb() {
        try (final Connection connection = DriverManager.getConnection(
                ClickhouseConfig.URL,
                ClickhouseConfig.USER,
                ClickhouseConfig.PASSWORD)
        ) {
            final Statement statement = connection.createStatement();
            final String sql = FileUtils.readFile(createScriptPath);
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {

        crateDb();

        try (final Connection connection = getConnection()) {
            final Statement statement = connection.createStatement();
            final String sql = FileUtils.readFile(initScriptPath);
            LOGGER.info("initialize clickhouse {}", initScriptPath);
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void sync(LocalDateTime now) {
        LOGGER.info("Синхронизация Clickhouse - дата обрезки - {}", now);
        LOGGER.info("Not implemented yet");
    }
}
