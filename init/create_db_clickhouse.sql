-- Clickhouse не создает автоматически бд (при наличии соотв переменной в .env)
-- На Github пишут, что раньше оно работало, но потом сломалось.
-- Поэтому руками будем БД создавать.

CREATE DATABASE IF NOT EXISTS krista_etl_clickhouse;
