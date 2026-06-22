package ru.lab.database;

import ru.lab.utils.FileUtils;

import java.util.Map;

public class SqlTemplateEngine {

    public static String process(String rawSql, Map<String, String> params) {

        if (params == null) {
            return rawSql;
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            rawSql = rawSql.replaceAll("\\{\\{"+ entry.getKey() + "\\}\\}", entry.getValue());
        }

        return rawSql;
    }

}
