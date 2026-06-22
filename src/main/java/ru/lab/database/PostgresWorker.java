package ru.lab.database;

import com.fasterxml.jackson.databind.util.NamingStrategyImpls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.PostgresConfig;
import ru.lab.dto.response.EBudgetResponseDto;
import ru.lab.service.flattener.FlattenerService;
import ru.lab.utils.FileUtils;

import java.sql.Connection;
import java.sql.DriverManager;
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
import java.util.Stack;
import java.util.stream.Collectors;


public class PostgresWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostgresWorker.class);
    private final FlattenerService flattenerService = new FlattenerService();

    private final static String initScriptPath = "db/postgres/init.sql";
    private final static String insertScriptPath = "db/postgres/insert.sql";
    private final static String selectColumnsScriptPath = "db/postgres/selectColumns.sql";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(PostgresConfig.URL, PostgresConfig.USER, PostgresConfig.PASSWORD);
    }

    public void initialize() {

        try (final Connection connection = getConnection()) {
            final Statement statement = connection.createStatement();
            final String sql = FileUtils.readFile(initScriptPath);
            LOGGER.info("initialize postgres {}", initScriptPath);
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillStatement(PreparedStatement ps, List<String> columns, Map<String, Object> values) throws SQLException {

        int i = 1;
        for (String column : columns) {
            final Object value = values.get(column);
            ps.setObject(i++, value);
        }
    }

    private Map<String, Object> prepareRow(EBudgetResponseDto dto, LocalDate from, LocalDate to, List<String> columns) {

        final  Map<String, Object> row = flattenerService.flat(dto);
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

    public void insertBatch(List<EBudgetResponseDto> rows, LocalDate from, LocalDate to) {

        if (rows.isEmpty()) {
            return;
        }

        final List<String> columnsList = getColumns();

        final String columns = String.join(",", columnsList);

        final String placeholders = columnsList.stream()
                .map(c -> "?")
                .collect(Collectors.joining(","));

        final String updates = columnsList.stream()
                .filter(c -> !"info_guid".equals(c))
                .map(c -> c + " = EXCLUDED." + c)
                .collect(Collectors.joining(","));


        final Map<String, String> params =  new HashMap<>();
        params.put("columns", columns);
        params.put("values", placeholders);
        params.put("updates", updates);

        final String rawSql = FileUtils.readFile(insertScriptPath);
        final String sql = SqlTemplateEngine.process(rawSql, params);

        try (Connection connection = getConnection()) {

            final PreparedStatement ps = connection.prepareStatement(sql);
            connection.setAutoCommit(false);

            int count = 0;

            for (EBudgetResponseDto dto : rows) {

                final Map<String, Object> values = prepareRow(dto, from, to, columnsList);
                fillStatement(ps, columnsList, values);
                ps.addBatch();

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


}
