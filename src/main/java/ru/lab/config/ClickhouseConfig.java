package ru.lab.config;

public class ClickhouseConfig {

    public static final String URL = System.getenv("CLICKHOUSE_URL");
    public static final String USER = System.getenv("CLICKHOUSE_USER");
    public static final String PASSWORD = System.getenv("CLICKHOUSE_PASSWORD");

}
