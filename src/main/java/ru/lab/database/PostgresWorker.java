package ru.lab.database;

import com.fasterxml.jackson.databind.util.NamingStrategyImpls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.PostgresConfig;
import ru.lab.dto.response.EBudgetResponseDto;
import ru.lab.service.flattener.FlattenerService;
import ru.lab.utils.FileUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 Класс для работы с базой данных Postgres.
 */
public class PostgresWorker extends DatabaseWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostgresWorker.class);
    private final FlattenerService flattenerService = new FlattenerService();

    private final static String initScriptPath = "db/postgres/init.sql";
    private final static String insertScriptPath = "db/postgres/insert.sql";
    private final static String selectColumnsScriptPath = "db/postgres/selectColumns.sql";

    public PostgresWorker() {
        super(PostgresConfig.URL, PostgresConfig.USER, PostgresConfig.PASSWORD);
    }

    public void initialize() {
        executeScript(initScriptPath);
    }

    /**
     * Метод получения названий всех столбцов таблицы, за исключением столбца id
     * @return Список названий столбцов
     */
    public List<String> getColumns() {

        final List<String> columns = new ArrayList<>();

        try (final Connection connection = getConnection()) {
            final String sql = FileUtils.readFile(selectColumnsScriptPath);
            final Statement ps = connection.createStatement();

            final ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                columns.add(rs.getString("column_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        columns.remove("id");
        return columns;
    }

    /**
     * Метод подготовки данных для вставки в таблицу
     * @param dto EBudgetResponseDto объект
     * @param from дата начала поиска
     * @param to дата конца поиска
     * @param columns список названий столбцов
     * @return Мапу соответствий (название столбца: значение)
     */
    private Map<String, Object> prepareRow(EBudgetResponseDto dto, LocalDate from, LocalDate to, List<String> columns) {

        final Map<String, Object> row = flattenerService.flat(dto);
        final Map<String, Object> casedRow = row.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> NamingStrategyImpls.SNAKE_CASE.translate(e.getKey()),
                        Map.Entry::getValue));

        final Map<String, Object> values = new LinkedHashMap<>();

        for (String column : columns) {
            if ("last_update_from".equals(column)) {
                values.put(column, from);
            }
            else if ("last_update_to".equals(column)) {
                values.put(column, to);
            }
            else if ("updated_at".equals(column)) {
                values.put(column, LocalDateTime.now());
            }
            else {
                values.put(column, casedRow.get(column)); // null, если ключа нет
            }
        }

        return values;
    }

    /**
     * Метод подготовки скрипта для вставки данных.
     * Берёт шаблонный скрипт и вставляет туда данные.
     *
     * @param columnsList Список имен столбцов
     * @return Подготовленный скрипт вставки
     */
    private String prepareBatchInsertSql(List<String> columnsList){

        // 'column_name1,column_name2,...' - названия всех столбцов через запятую
        final String columns = String.join(",", columnsList);

        // '?,?,...' - ? на каждый столбец
        final String placeholders = columnsList.stream()
                .map(c -> "?")
                .collect(Collectors.joining(","));

        // 'column_name1 = EXCLUDED.column_name1, ... '
        // Но не берем столбец info_guid т.к. ON CONFLICT по нему.
        final String updates = columnsList.stream()
                .filter(c -> !"info_guid".equals(c))
                .map(c -> c + " = EXCLUDED." + c)
                .collect(Collectors.joining(","));


        final Map<String, String> params =  new HashMap<>();
        params.put("columns", columns);
        params.put("values", placeholders);
        params.put("updates", updates);

        final String rawSql = FileUtils.readFile(insertScriptPath);
        return SqlTemplateEngine.process(rawSql, params);
    }

    /**
     * Метод вставки набора объектов в таблицу
     *
     * @param rows Список с объектами EBudgetResponseDto
     * @param from дата начала поиска
     * @param to дата конца поиска
     */
    public void insertBatch(List<EBudgetResponseDto> rows, LocalDate from, LocalDate to) {

        if (rows == null || rows.isEmpty()) {
            return;
        }

        final List<String> columnsList = getColumns();
        final String sql = prepareBatchInsertSql(columnsList);

        try (Connection connection = getConnection()) {

            final PreparedStatement ps = connection.prepareStatement(sql);
            connection.setAutoCommit(false);

            int count = 0;
            for (EBudgetResponseDto dto : rows) {

                final Map<String, Object> values = prepareRow(dto, from, to, columnsList);
                fillStatement(ps, columnsList, values);
                ps.addBatch();

                // Ограничение размера батча
                if (++count % 1000 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            ps.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод подготовки выражение для вставки.
     * Заменяет '?' на нужные данные.
     *
     * @param ps Запрос с параметрами
     * @param columns Список имен столбцов
     * @param values Список значений для вставки
     */
    private void fillStatement(PreparedStatement ps, List<String> columns, Map<String, Object> values) throws SQLException {

        int i = 1;
        for (String column : columns) {
            final Object value = values.get(column);
            ps.setObject(i++, value);
        }
    }



}
