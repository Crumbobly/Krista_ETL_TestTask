package ru.lab.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.ClickhouseConfig;
import ru.lab.utils.FileUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
Класс для работы с базой данных ClickHouse.
 */
public class ClickhouseWorker extends DatabaseWorker {

    private final static String initScriptPath = "db/clickhouse/init.sql";
    private final static String insertScriptPath = "db/clickhouse/insert.sql";
    private final static String selectColumnsScriptPath = "db/clickhouse/selectColumns.sql";

    public ClickhouseWorker() {
        super(ClickhouseConfig.URL, ClickhouseConfig.USER, ClickhouseConfig.PASSWORD);
    }

    /**
     * Метод для запуска скрипта инициализации (создания таблицы и пр.)
     */
    public void initialize() {
        executeScript(initScriptPath);
    }


    /**
     * Пакетная вставка данных в ClickHouse.
     *
     * <p>Данные вставляются батчами по 1000 записей для оптимизации производительности.</p>
     *
     * @param rows список строк для вставки, где каждая строка представлена как Map:
     *             ключ — имя колонки, значение — значение поля
     */
    public void insertBatch(List<Map<String, Object>> rows) {

        /*
        Вообще ClickHouse умеет работать с бинарным потоком данных,
        но для тестового варианта - текущая реализация вставки приемлема.
         */

        if (rows == null || rows.isEmpty()) {
            return;
        }

        final List<String> columnsList = getColumns(selectColumnsScriptPath);
        final String sql = prepareBatchInsertSql(columnsList);

        try (final Connection connection = getConnection();
             final PreparedStatement ps = connection.prepareStatement(sql))
        {
            int count = 0;
            for (Map<String, Object> values : rows) {

                fillStatement(ps, columnsList, values);
                ps.addBatch();

                if (++count % 1000 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Формирует SQL-запрос для пакетной вставки.
     *
     * <p>Использует шаблон SQL из файла и подставляет: список колонок и плейсхолдеры (?) для значений</p>
     *
     * @param columnsList список колонок таблицы
     * @return готовый SQL-запрос для batch insert
     */
    private static String prepareBatchInsertSql(List<String> columnsList) {

        final String columns = String.join(",", columnsList);

        final String placeholders = columnsList.stream()
                .map(c -> "?")
                .collect(Collectors.joining(","));

        final Map<String, String> params = new HashMap<>();
        params.put("columns", columns);
        params.put("values", placeholders);

        final String rawSql = FileUtils.readFile(insertScriptPath);
        return SqlTemplateEngine.process(rawSql, params);
    }
}
