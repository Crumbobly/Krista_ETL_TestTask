package ru.lab.config;

public class ClickhouseConfig {

    public static final String URL = System.getenv("CLICKHOUSE_URL");
    public static final String URL_DB = System.getenv("CLICKHOUSE_URL_DB");

    public static final String DB = System.getenv("CLICKHOUSE_DB");
    public static final String TABLE_NAME = System.getenv("CLICKHOUSE_TABLE_NAME");
    public static final String USER = System.getenv("CLICKHOUSE_USER");
    public static final String PASSWORD = System.getenv("CLICKHOUSE_PASSWORD");

}
