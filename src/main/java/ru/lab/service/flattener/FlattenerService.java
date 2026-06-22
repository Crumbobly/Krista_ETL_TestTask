package ru.lab.service.flattener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FlattenerService {

    public final static String DELIMITER = "#";

    public Map<String, Object> flat(Object object) {

        final Map<String, Object> result = new LinkedHashMap<>();
        final Map<String, List<Object>> tmp = new LinkedHashMap<>();
        flat(object, "", tmp);

        for (Map.Entry<String, List<Object>> entry : tmp.entrySet()) {
            final String key = entry.getKey();
            final List<Object> value = entry.getValue();

            if (value == null || value.isEmpty()) {
                result.put(key, null);
            } else if (value.size() == 1) {
                result.put(key, value.get(0));
            } else {
                final StringBuilder sb = new StringBuilder();
                for (Object o : value) {
                    sb.append(o.toString()).append(DELIMITER);
                }
                result.put(key, sb.substring(0, sb.length() - 1));
            }
        }

        return result;
    }

    private Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void flat(Object object, String prefix, Map<String, List<Object>> map) {

        if (object == null) return;

        for (Field field : object.getClass().getDeclaredFields()) {

            if (field.isSynthetic()) {
                continue;
            }

            final String fieldName = field.getName();
            final Object fieldValue = getFieldValue(field, object);

            if (fieldValue == null) {
                continue;
            }

            if (fieldValue instanceof Flattenable) {
                flat(fieldValue, prefix + fieldName + "_", map);
            } else if (fieldValue instanceof Iterable<?>) {
                for (Object element : (Iterable<?>) fieldValue) {
                    if (element == null) {
                        continue;
                    }
                    if (element instanceof Flattenable) {
                        flat(element, prefix + fieldName + "_", map);
                    } else {
                        map.computeIfAbsent(prefix + fieldName, k -> new ArrayList<>()).add(element);
                    }
                }
            } else {
                map.computeIfAbsent(prefix + fieldName, k -> new ArrayList<>()).add(fieldValue);
            }

        }
    }

}
