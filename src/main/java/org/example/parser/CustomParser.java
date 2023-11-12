package org.example.parser;

import lombok.SneakyThrows;
import org.example.pojo.Order;
import org.example.pojo.Product;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.helpers.StringUtils.customTrim;

public class CustomParser {
    private static final String DOUBLE_CLASS_NAME = "class java.lang.Double";
    private static final String LIST_INTERFACE_NAME = "interface java.util.List";

    public static String serialize(Object obj) {
        Map<String, Object> map = new LinkedHashMap<>();
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

    @SneakyThrows
    public static <T> T deserialize(Class<T> clazz, String jsonString) {
        T object = clazz.getDeclaredConstructor().newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            Map.Entry<String, Object> entry = getKeyValueFromJson(field, jsonString);
            setFieldValue(object, entry.getKey(), entry.getValue());
        }
        return object;
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
                value instanceof Timestamp ||
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

    private static Map.Entry<String, Object> getKeyValueFromJson(Field field, String json) {
        if (LIST_INTERFACE_NAME.equals(field.getType().toString())) {
            return parseListElements(field, json);
        }
        return getEntryForParseSimpleElem(field, json);
    }

    private static Map.Entry<String, Object> getEntryForParseSimpleElem(Field field, String json) {
        String regexp = String.format("\"%s\":\".*?\"[,\\}]", field.getName());
        String split = "\":\"";
        if (field.getType().toString().equals(DOUBLE_CLASS_NAME)) {
            regexp = String.format("\"%s\":.*?[\\},\\]\\n]", field.getName());
            split = "\":";
        }
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String[] keyVal = matcher.group().split(split);
            return Map.entry(customTrim(keyVal[0], "\""),
                    customTrim(keyVal[1], "\"")
                            .replace("\",", "")
                            .replace("}", "")
                            .replace("\"", ""));
        }
        return Map.entry(field.getName(), null);
    }

    @SneakyThrows
    private static Map.Entry<String, Object> parseListElements(Field field, String json) {
        List<Object> resultList = new ArrayList<>();
        Type elementType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        Class<?> elementClass = Class.forName(elementType.getTypeName());

        Pattern pattern = Pattern.compile(getRegexpForClassFields(elementClass));

        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String partJson =  matcher.group();
            resultList.add(deserialize(elementClass,partJson));
        }
        return Map.entry(field.getName(), resultList);
    }

    private static void setFieldValue(Object instance, String fieldName, Object fieldValue) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (fieldType == UUID.class) {
                field.set(instance, UUID.fromString(fieldValue.toString()));
            } else if (fieldType == String.class) {
                field.set(instance, fieldValue.toString());
            } else if (fieldType == Double.class) {
                field.set(instance, Double.valueOf(fieldValue.toString().replace(",", "")));
            } else if (fieldType == LocalDate.class) {
                field.set(instance, LocalDate.parse(fieldValue.toString()));
            } else if (fieldType == Timestamp.class) {
                field.set(instance, Timestamp.valueOf(fieldValue.toString()));
//            } else if (fieldType == Timestamp.class) {
//                field.set(instance, Timestamp.valueOf(fieldValue.toString().replace(" ", "T")));
            } else if (fieldType == List.class) {
                field.set(instance, (List) fieldValue);
            } else {

            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

//    private static String getJsonForClass(Class<?> clazz, String json) {
//        String regexp = getRegexpForClassFields(clazz);
//        Pattern pattern = Pattern.compile(regexp);
//        Matcher matcher = pattern.matcher(json);
//        if (matcher.find()) {
//            return matcher.group();
//        }
//        throw new IllegalArgumentException("Incorrect json string for match by - " + regexp);
//    }

    private static String getRegexpForClassFields(Class<?> clazz) {
        StringBuilder builder = new StringBuilder("\\{");
        for (Field field: clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (builder.length() > 2) {
                builder.append(",");
            }

            if (fieldType == Double.class) {
                builder.append(String.format("\"%s\":.*?", field.getName()));
            } else if (fieldType == List.class) {
                builder.append(String.format("\"%s\":\\[.*?\\]", field.getName()));
            }else if (fieldType == UUID.class) {
                builder.append(String.format("\"%s\":\"(?:(?!:|\").)*\"", field.getName()));

            } else {
                builder.append(String.format("\"%s\":\".*?\"", field.getName()));
            }
        }
        builder.append("\\}");
        return builder.toString();
    }
}
