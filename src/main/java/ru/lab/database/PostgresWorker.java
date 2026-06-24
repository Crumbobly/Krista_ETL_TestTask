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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 Класс для работы с базой данных Postgres.
 */
public class PostgresWorker extends DatabaseWorker {

    private final FlattenerService flattenerService = new FlattenerService();
    private final int FETCH_BATCH_SIZE = 1000;

    private final static String initScriptPath = "db/postgres/init.sql";
    private final static String insertScriptPath = "db/postgres/insert.sql";
    private final static String selectColumnsScriptPath = "db/postgres/selectColumns.sql";
    private final static String selectBatchByUpdateAtScriptPath = "db/postgres/selectBatchByUpdatedAt.sql";

    public PostgresWorker() {
        super(PostgresConfig.URL, PostgresConfig.USER, PostgresConfig.PASSWORD);
    }


    /**
     * Метод для запуска скрипта инициализации (создания таблицы и пр.)
     */
    public void initialize() {
        executeScript(initScriptPath);
    }


    /**
     * Преобразует DTO в структуру Map<String, Object>,
     * пригодную для вставки в БД.
     *
     * <p>
     * Использует FlattenerService для разворачивания DTO
     * и приводит ключи к snake_case.
     * </p>
     *
     * @param dto входной объект EBudgetResponseDto
     * @param from дата начала диапазона
     * @param to дата конца диапазона
     * @param columns список колонок таблицы
     * @return map колонка → значение
     */
    private Map<String, Object> prepareRow(EBudgetResponseDto dto, LocalDate from, LocalDate to, List<String> columns) {

        final Map<String, Object> row = flattenerService.flat(dto);
        final Map<String, Object> casedRow = new LinkedHashMap<>();

        for (Map.Entry<String, Object> e : row.entrySet()) {
            casedRow.put(
                    NamingStrategyImpls.SNAKE_CASE.translate(e.getKey()),
                    e.getValue()
            );
        }

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
     * Выполняет batch-вставку данных в Postgres.
     *
     * <p>
     * Разбивает данные на батчи по 1000 записей.
     * Использует PreparedStatement + batch execution.
     * </p>
     *
     * @param rows список DTO для вставки
     * @param from дата начала диапазона
     * @param to дата конца диапазона
     */
    public void insertBatch(List<EBudgetResponseDto> rows, LocalDate from, LocalDate to) {

        if (rows == null || rows.isEmpty()) {
            return;
        }

        final List<String> columnsList = getColumns(selectColumnsScriptPath);
        columnsList.remove("id");

        final String sql = prepareBatchInsertSql(columnsList);

        try (final Connection connection = getConnection();
             final PreparedStatement ps = connection.prepareStatement(sql))
        {

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
     * Получает батч данных из Postgres по полю updated_at.
     *
     * <p>
     * Используется для инкрементальной синхронизации.
     * </p>
     *
     * <b>
     *  На самом деле эта часть является узким местом, и в идеале это нужно оптимизировать.
     *
     * </b>
     *
     * @param cutUpdatedAtTime точка отсечения
     * @return список строк результата
     */
    public List<Map<String, Object>> streamByUpdatedAt(LocalDateTime cutUpdatedAtTime, Consumer<List<Map<String, Object>>> consumer) {

        final String sql = FileUtils.readFile(selectBatchByUpdateAtScriptPath);
        final List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection connection = getConnection();
             final PreparedStatement ps = connection.prepareStatement(sql))
        {
            connection.setAutoCommit(false);
            ps.setObject(1, cutUpdatedAtTime);
            ps.setFetchSize(FETCH_BATCH_SIZE);

            final ResultSet rs = ps.executeQuery();
            final ResultSetMetaData metaData = rs.getMetaData();
            final int columnCount = metaData.getColumnCount();
            final List<Map<String, Object>> batch = new ArrayList<>();

            while (rs.next()) {

                final  Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    final String columnName = metaData.getColumnName(i);
                    final Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                batch.add(row);

                if (batch.size() == FETCH_BATCH_SIZE) {
                    consumer.accept(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                consumer.accept(batch);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rows;
    }


    /**
     * Формирует SQL-запрос для пакетной вставки.
     *
     * <p>Использует шаблон SQL из файла и подставляет: список колонок и плейсхолдеры (?) для значений</p>
     *
     * @param columnsList список колонок таблицы
     * @return готовый SQL-запрос для batch insert
     */
    private static String prepareBatchInsertSql(List<String> columnsList){

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
}
