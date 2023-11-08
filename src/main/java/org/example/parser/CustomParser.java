package org.example.parser;

import org.example.pojo.Order;
import org.example.pojo.Product;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomParser {

    public static String serialize(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object value = field.get(obj);
                if (value != null) {
                    map.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return mapToJson(map);
    }

    private static String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append("\"" + entry.getKey() + "\":");
            sb.append(valueToJson(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    private static String valueToJson(Object value) {
        if (value instanceof String ||
                value instanceof LocalDateTime ||
                value instanceof OffsetDateTime ||
                value instanceof LocalDate ||
                value instanceof UUID) {
            return "\"" + value + "\"";
        } else if (value instanceof Integer ||
                value instanceof Double) {
            return value.toString();
        } else if (value instanceof Product) {
            return serialize(value);
        } else if (value instanceof Order) {
            return serialize(value);
        } else if (value instanceof List<?>) {
            return listToJson((List<?>) value);
        } else if (value instanceof Map<?, ?>) {
            return mapToJson((Map<String, Object>) value);
        } else {
            return "";
        }
    }

    private static String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object item : list) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(serialize(item));
        }
        sb.append("]");
        return sb.toString();
    }
}
