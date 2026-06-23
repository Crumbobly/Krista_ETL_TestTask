package ru.lab.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс-фабрика для создания объектов ObjectMapper для сериализации\десериализации JSON
 */
public final class ObjectMapperFactory {

    private static final ObjectMapper MAPPER = create();

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    private static ObjectMapper create() {

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        final JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateTimeFormatter));
        module.addDeserializer(Boolean.class, new BooleanDeserializer());

        // WRITE_DATES_AS_TIMESTAMPS - чтобы даты писались как даты, а не как timestamp
        // FAIL_ON_UNKNOWN_PROPERTIES - игнорируем неизвестные атрибуты
        // ACCEPT_CASE_INSENSITIVE_PROPERTIES - не важно какой регистр
        return JsonMapper.builder()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .build().
                registerModule(module);
    }


    // Кастомный десериализатор, т.к. в качестве Boolean может прийти и (0 или 1).
    private static class BooleanDeserializer extends JsonDeserializer<Boolean> {

        @Override
        public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText();

            if ("1".equals(value)) {
                return Boolean.TRUE;
            } else if ("0".equals(value)) {
                return Boolean.FALSE;
            }

            return Boolean.parseBoolean(value);
        }
    }

}
