package ru.lab.database.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.config.DBConfig;
import ru.lab.database.DatabaseWorker;
import ru.lab.database.SqlTemplateEngine;
import ru.lab.utils.FileUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class DatabaseWorkerImpl implements DatabaseWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseWorkerImpl.class);

    private final static String initScriptPathScript = "db/init.sql";
    private final static String selectColumnsNamesScript = "db/select_columns_names.sql";
    private final static String addColumnScript = "db/add_column.sql";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }

    public void initialize() {

        try (final Connection connection = getConnection()) {
            final Statement statement = connection.createStatement();
            final String[] params = new String[]{DBConfig.TABLE_NAME};
            final String rawSql = FileUtils.readFile(initScriptPathScript);
            final String sql = SqlTemplateEngine.process(rawSql, params);
            LOGGER.info("initialize {}, {}", initScriptPathScript, Arrays.toString(params));

            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Set<String> getColumns() {

        final Set<String> columns = new HashSet<>();

        try (final Connection connection = getConnection()) {
            final Statement statement = connection.createStatement();

            final String[] params = new String[]{DBConfig.TABLE_NAME};
            final String rawSql = FileUtils.readFile(selectColumnsNamesScript);
            final String sql = SqlTemplateEngine.process(rawSql, params);

            LOGGER.info("getColumns {}, {}", selectColumnsNamesScript, Arrays.toString(params));

            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    columns.add(rs.getString("column_name"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return columns;
    }

    public void addColumns(Set<String> columns) {
        try (final Connection connection = getConnection()) {
            final Statement statement = connection.createStatement();

            for (String column : columns) {
                // Сейчас тут стоит TEXT, но в будущем можно сделать, чтобы мы могли заранее понимать тип нового столбца.
                // Для этого в EBudgetFlattenerService придётся еще проверять на базовые типы и хранить соответствия.
                final String[] params = new String[]{DBConfig.TABLE_NAME, column, "TEXT"};
                final String rawSql = FileUtils.readFile(addColumnScript);
                final String sql = SqlTemplateEngine.process(rawSql, params);

                LOGGER.info("addColumns {}, {}", addColumnScript, Arrays.toString(params));
                // batch in future
                statement.execute(sql);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void addMissingColumns(Set<String> columns) {
        Set<String> existingColumns = getColumns();
        Set<String> missingColumns = new HashSet<>(columns);

        missingColumns.removeAll(existingColumns);
        if (!missingColumns.isEmpty()) {
            addColumns(missingColumns);
        }
    }
}
