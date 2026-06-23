package ru.lab.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс конфигурации Postgres.
 * Содержит статические поля с данными для подключения к БД.
 * Значения берутся из переменных окружения
 */
public class PostgresConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresConfig.class);

    public static final String URL = System.getenv("POSTGRES_URL");
    public static final String USER = System.getenv("POSTGRES_USER");
    public static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");

    static {
        if (URL == null || USER == null || PASSWORD == null) {
            LOGGER.error("Не заданы переменные окружения для запуска Postgres");
            throw new IllegalStateException("Не заданы переменные окружения для Postgres");
        }
    }

}
