package org.example.parser;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.example.parser.pojoTest.CustomerTest;
import org.example.pojo.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CustomParserTest {


    @Test
    void serializeWithCorrectParametersSimpleTest() {
        //Given
        String expected = "{\"firstName\":\"SimpleName\",\"lastName\":\"SimpleLastName\",\"orders\":[{\"id\":\"a3b125bb-0889-4fcb-8e32-e2b3c42caebf\",\"products\":[{\"price\":1.22,\"name\":\"Prod1\",\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\"},{\"price\":2.33,\"name\":\"Prod2\",\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\"},{\"price\":3.44,\"name\":\"Prod3\",\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\"}],\"createDate\":\"2023-11-08T23:48:53.223505600+03:00\"},{\"id\":\"7572a3a6-e460-4629-a45e-e14150eebce7\",\"products\":[{\"price\":1.22,\"name\":\"Prod1\",\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\"},{\"price\":2.33,\"name\":\"Prod2\",\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\"},{\"price\":3.44,\"name\":\"Prod3\",\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\"}],\"createDate\":\"2023-11-08T23:48:00.395060100+03:00\"}],\"id\":\"5f4b9633-a0a4-4788-8a6a-2cb5900a7ce1\",\"dateBirth\":\"2023-11-08\"}";
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
        gsonBuilder.registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter());

        String expected = gsonBuilder.create().toJson(dto);

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
        return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}

class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

    public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}