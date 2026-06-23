package ru.lab.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab.utils.FileUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Базовый абстрактный класс для работы с SQL-базами данных.
 */
public abstract class DatabaseWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseWorker.class);

    private final String url;
    private final String username;
    private final String password;

    public DatabaseWorker(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Создает новое JDBC соединение с базой данных.
     *
     * @return открытое соединение
     * @throws SQLException если соединение не удалось установить
     */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Получает список колонок таблицы через SQL-скрипт.
     *
     * <p>Обычно используется запрос к information_schema или system таблицам.</p>
     *
     * @param selectColumnsScriptPath путь к SQL-файлу, возвращающему список колонок
     * @return список имен колонок таблицы
     */
    public List<String> getColumns(String selectColumnsScriptPath) {

        final List<String> columns = new ArrayList<>();

        try (final Connection connection = getConnection();
             final Statement ps = connection.createStatement())
        {
            final String sql = FileUtils.readFile(selectColumnsScriptPath);
            final ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                columns.add(rs.getString("column_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return columns;
    }

    /**
     * Выполняет SQL-скрипт без возврата результата.
     *
     * @param scriptPath путь к SQL-скрипту
     */
    public void executeScript(String scriptPath){
        try (final Connection connection = getConnection();
             final Statement statement = connection.createStatement();)
        {
            final String sql = FileUtils.readFile(scriptPath);
            LOGGER.info("Выполнение скрипта {}", scriptPath);
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Заполняет PreparedStatement значениями из Map по порядку колонок.
     *
     * <p><b>Важно: </b>порядок значений строго соответствует списку колонок.</p>
     *
     * @param ps PreparedStatement с SQL-запросом
     * @param columns список колонок (задаёт порядок параметров)
     * @param values значения строки (column -> value)
     * @throws SQLException при ошибке установки параметров
     */
    protected void fillStatement(PreparedStatement ps, List<String> columns, Map<String, Object> values) throws SQLException {

        int i = 1;
        for (String column : columns) {
            final Object value = values.get(column);
            ps.setObject(i++, value);
        }
    }


}
