package ru.lab.database;

import ru.lab.utils.FileUtils;

public class SqlTemplateEngine {

    public static String process(String rawSql, String[] params) {

        if (params == null) {
            return rawSql;
        }

        for (String param : params) {
            rawSql = rawSql.replaceFirst("\\{\\{.*?\\}\\}", param);
        }

        return rawSql;
    }

}
