package ru.lab.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс конфигурации архивирования.
 * Значения берутся из переменных окружения
 */
public class ArchiveConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveConfig.class);

    public static final String ARCHIVE_FOLDER = System.getenv("ARCHIEVE_FOLDER");

    static {
        if (ARCHIVE_FOLDER == null) {
            LOGGER.error("Не заданы переменные окружения для архивирования");
            throw new IllegalStateException("Не заданы переменные окружения для архивирования");
        }
    }

}
