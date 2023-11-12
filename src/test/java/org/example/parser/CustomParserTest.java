package org.example.parser;

import com.google.gson.*;
import org.example.parser.pojoTest.CustomerTest;
import org.example.pojo.Customer;
import org.example.pojo.test.Elem1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.example.parser.CustomParser.deserialize;
import static org.example.parser.pojoTest.Elem1Test.getElem1;

class CustomParserTest {


    @Test
    void serializeWithCorrectParametersSimpleTest() {
        //Given
        String expected = "{\"id\":\"5f4b9633-a0a4-4788-8a6a-2cb5900a7ce1\",\"firstName\":\"SimpleName\",\"lastName\":\"SimpleLastName\",\"dateBirth\":\"2023-11-08\",\"orders\":[{\"id\":\"a3b125bb-0889-4fcb-8e32-e2b3c42caebf\",\"products\":[{\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\",\"name\":\"Prod1\",\"price\":1.22},{\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\",\"name\":\"Prod2\",\"price\":2.33},{\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\",\"name\":\"Prod3\",\"price\":3.44}],\"createDate\":\"2023-11-09 09:08:00.0\"},{\"id\":\"7572a3a6-e460-4629-a45e-e14150eebce7\",\"products\":[{\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\",\"name\":\"Prod1\",\"price\":1.22},{\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\",\"name\":\"Prod2\",\"price\":2.33},{\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\",\"name\":\"Prod3\",\"price\":3.44}],\"createDate\":\"2023-11-09 09:09:00.0\"}]}";
        Customer dto = CustomerTest.builder().build().buildCustomer();

        //When
        String actual = CustomParser.serialize(dto);

        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void serializeWithCorrectParametersSimpleJ() {
        //Given
        Customer dto = CustomerTest.builder().build().buildCustomer();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampAdapter());

        String expected = gsonBuilder.create().toJson(dto);

        //When
        String actual = CustomParser.serialize(dto);

        //Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deserializeTestClassByGson() {
        //Given
        Elem1 expected = getElem1();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampAdapter());
        String json = gsonBuilder.create().toJson(expected);

        //When
        Elem1 actual = deserialize(Elem1.class, json);


        //Then
        Assertions.assertEquals(expected, actual);

    }
    @Test
    void deserializeTestClassFromClass() {
        //Given
        Customer expected = CustomerTest.builder().build().buildCustomer1();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Timestamp.class, new TimestampAdapter());

        String json = gsonBuilder.create().toJson(expected);

        //When
        Customer actual = deserialize(Customer.class, json);

        //Then
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void deserializeTestClassFromString() {
        //Given
        String jsonString = "{\"id\":\"5f4b9633-a0a4-4788-8a6a-2cb5900a7ce1\",\"firstName\":\"SimpleName\",\"lastName\":\"SimpleLastName\",\"dateBirth\":\"2023-11-08\",\"orders\":[{\"id\":\"a3b125bb-0889-4fcb-8e32-e2b3c42caebf\",\"products\":[{\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\",\"name\":\"Prod1\",\"price\":1.22},{\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\",\"name\":\"Prod2\",\"price\":2.33},{\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\",\"name\":\"Prod3\",\"price\":3.44}],\"createDate\":\"2023-11-09 09:08:00.0\"},{\"id\":\"7572a3a6-e460-4629-a45e-e14150eebce7\",\"products\":[{\"id\":\"ab41ede9-a75a-4ebe-b6d0-f0363ee0aa1c\",\"name\":\"Prod1\",\"price\":1.22},{\"id\":\"71934ae7-3cf3-48db-aeaa-8764ecae4c3b\",\"name\":\"Prod2\",\"price\":2.33},{\"id\":\"308db6bc-2a07-42ab-83a1-dc6f4c7ded52\",\"name\":\"Prod3\",\"price\":3.44}],\"createDate\":\"2023-11-09 09:09:00.0\"}]}";
        Customer expected = CustomerTest.builder().build().buildCustomer();

        //When
        Customer actual = deserialize(Customer.class, jsonString);

        //Then
        Assertions.assertEquals(expected, actual);

    }
}

class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.toString());
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String dateString = json.getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
}

class TimestampAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

    private final SimpleDateFormat dateFormat;

    public TimestampAdapter() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    }

    @Override
    public JsonElement serialize(Timestamp src, Type typeOfSrc, JsonSerializationContext context) {
        String formattedTimestamp = dateFormat.format(src);
        return new JsonPrimitive(formattedTimestamp);
    }

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            Date parsedDate = dateFormat.parse(json.getAsString());
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
