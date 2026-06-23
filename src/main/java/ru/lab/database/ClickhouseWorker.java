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

/**
Класс для работы с базой данных ClickHouse.
 */
public class ClickhouseWorker extends DatabaseWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClickhouseWorker.class);
    private final static String initScriptPath = "db/clickhouse/init.sql";

    public ClickhouseWorker() {
        super(ClickhouseConfig.URL, ClickhouseConfig.USER, ClickhouseConfig.PASSWORD);
    }

    public void initialize() {
        executeScript(initScriptPath);
    }

    /**
     * Выгрузка данных в ClickHouse.
     * <p>
     * --- Not implemented yet ---
     *
     * @param cutTime Время последней актуальной записи.
     */
    public void sync(LocalDateTime cutTime) {
        LOGGER.info("Синхронизация Clickhouse - дата обрезки - {}", cutTime);
        LOGGER.info("Not implemented yet");
        //  Тут должно быть примерно это:
        //  Делаем select (с offset) где updated_at >= cutTime
        //  И загружаем эти куски по очереди в Clickhouse
    }
}
