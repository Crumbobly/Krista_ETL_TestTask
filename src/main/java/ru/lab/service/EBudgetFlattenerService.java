package ru.lab.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.util.NamingStrategyImpls;
import ru.lab.dto.EBudgetResponseDto;
import ru.lab.dto.FlattenerDto;
import ru.lab.dto.inner.InfoDto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EBudgetFlattenerService {

    public Map<String, Object> flat(Object object) {
        final Map<String, Object> flatMap = new LinkedHashMap<>();
        flat(object, "", flatMap);
        return flatMap;
    }

    // Первый раз на практике пригодилась рефлексия
    private void flat(Object object, String prefix, Map<String, Object> flatMap) {

        if (object == null) {
            return;
        }

        for (Field field : object.getClass().getDeclaredFields()) {

            field.setAccessible(true);
            Object value;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (value == null) {
                continue;
            }

            final String fieldName = NamingStrategyImpls.SNAKE_CASE.translate(field.getName());
            field.setAccessible(false);


            if (value instanceof List<?>) {
                final List<?> list = (List<?>) value;
                for (int i = 0; i < list.size(); i++) {
                    final Object element = list.get(i);
                    flat(element, prefix + fieldName + "_" + (i + 1) + "_", flatMap);
                }
            }

            else if (value instanceof FlattenerDto) {
                flat(value, prefix + fieldName + "_", flatMap);
            }

            else{
                flatMap.put(prefix + fieldName, value);
            }


        }
    }

}
