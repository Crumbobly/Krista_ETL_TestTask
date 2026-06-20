package ru.lab.config;

public final class DBConfig {

    public static final String URL = System.getenv("POSTGRES_URL");
    public static final String TABLE_NAME = System.getenv("POSTGRES_TABLE_NAME");
    public static final String USER = System.getenv("POSTGRES_USER");
    public static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");

}
