package ru.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.database.ClickhouseWorker;
import ru.lab.database.PostgresWorker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Сервис для синхронизации БД.
 */
public class DBSyncService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBSyncService.class);

    private final PostgresWorker postgresWorker;
    private final ClickhouseWorker clickhouseWorker;

    public DBSyncService(PostgresWorker postgresWorker, ClickhouseWorker clickhouseWorker) {
        this.postgresWorker = postgresWorker;
        this.clickhouseWorker = clickhouseWorker;
    }

    /**
     * Метод синхронизации Postgres с Clickhouse.
     * Выгружает данные из Postgres батчами и загружает их в Clickhouse
     *
     * @param cutTime Время последней актуальной записи.
     */
    public void sync(LocalDateTime cutTime) {
        LOGGER.info("Синхронизация Clickhouse - дата обрезки - {}", cutTime);

        postgresWorker.streamByUpdatedAt(
                cutTime,
                clickhouseWorker::insertBatch
        );

        LOGGER.info("Синхронизация завершена");
    }
}
