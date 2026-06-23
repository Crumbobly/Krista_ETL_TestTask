package ru.lab.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс конфигурации ClickHouse.
 * Содержит статические поля с данными для подключения к БД.
 * Значения берутся из переменных окружения
 */
public class ClickhouseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClickhouseConfig.class);

    public static final String URL = System.getenv("CLICKHOUSE_URL");
    public static final String USER = System.getenv("CLICKHOUSE_USER");
    public static final String PASSWORD = System.getenv("CLICKHOUSE_PASSWORD");

    static {
        if (URL == null || USER == null || PASSWORD == null) {
            LOGGER.error("Не заданы переменные окружения для запуска ClickHouse");
            throw new IllegalStateException("Не заданы переменные окружения для ClickHouse");
        }
    }

}
