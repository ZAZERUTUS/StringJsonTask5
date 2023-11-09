package org.example.parser;

import com.google.gson.*;
import org.example.parser.pojoTest.CustomerTest;
import org.example.pojo.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Comparator;

class CustomParserTest {


    @Test
    void serializeWithCorrectParametersSimpleTest() {
        //Given
        String expected = "{\"id\":\"5f4b9633-a0a4-4788-8a6a-2cb5900a7ce1\",\"firstName\":\"SimpleName\",\"lastName\":\"SimpleLastName\",\"dateBirth\":\"2023-11-08\",\"orders\":[{\"id\":\"a3b125bb-0889-4fcb-8e32-e2b3c42caebf\",\"products\":[{\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\",\"name\":\"Prod1\",\"price\":1.22},{\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\",\"name\":\"Prod2\",\"price\":2.33},{\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\",\"name\":\"Prod3\",\"price\":3.44}],\"createDate\":\"2023-11-09 09:08:00.0\"},{\"id\":\"7572a3a6-e460-4629-a45e-e14150eebce7\",\"products\":[{\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\",\"name\":\"Prod1\",\"price\":1.22},{\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\",\"name\":\"Prod2\",\"price\":2.33},{\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\",\"name\":\"Prod3\",\"price\":3.44}],\"createDate\":\"2023-11-09 09:09:00.0\"}]}";
        Customer dto = CustomerTest.builder().build().buildCustomer();

        //When
        String actual = CustomParser.serialize(dto);
        System.out.println(actual);
        System.out.println(expected);
        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void serializeWithCorrectParametersSimpleJ() {
        //Given
        Customer dto = CustomerTest.builder().build().buildCustomer();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(TimestampAdapter.class, new TimestampAdapter());

        String expected = gsonBuilder.setFieldNamingStrategy(new AlphabeticalFieldNamingStrategy()).create().toJson(dto);

        //When
        String actual = CustomParser.serialize(dto);
        System.out.println(actual);
        System.out.println(expected);
        //Then
        Assertions.assertEquals(expected, actual);
    }
}

class LocalDateAdapter implements JsonSerializer<LocalDate> {

    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.toString());
    }
}

class TimestampAdapter implements JsonSerializer<Timestamp> {

    public JsonElement serialize(Timestamp date, Type typeOfSrc, JsonSerializationContext context) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a");

        return new JsonPrimitive(formatter.format((TemporalAccessor) date));
    }
}

class AlphabeticalFieldNamingStrategy implements FieldNamingStrategy {
    @Override
    public String translateName(Field field) {
        return Arrays.stream(field.getDeclaringClass().getDeclaredFields())
                .sorted(Comparator.comparing(Field::getName))
                .filter(f -> f.getName().equals(field.getName()))
                .findFirst()
                .map(Field::getName)
                .orElse(field.getName());
    }
}