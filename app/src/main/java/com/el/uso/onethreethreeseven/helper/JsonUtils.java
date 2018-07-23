package com.el.uso.onethreethreeseven.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

class DateTimeTypeAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

    public JsonElement serialize(DateTime src, Type typeOfT, JsonSerializationContext context) {
        // Compatible for .NET DateTime.ParseExact(str, "o", CultureInfo.InvariantCulture, DateTimeStyles.AdjustToUniversal);
        // It requires exact 7 bit of fraction.
        StringBuffer sb = new StringBuffer();
        FMT.printTo(sb, src);
        int pos = sb.length() - 1;
        return new JsonPrimitive(sb.deleteCharAt(pos).append("0000Z").toString());
    }

    public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date time should be a string value");
        }
        String jsonString = json.getAsString();
        if (!jsonString.contains(".")) {
            jsonString = jsonString.replace("Z", ".000Z");
        } else if (!jsonString.contains("+") && !jsonString.contains("Z"))
            jsonString = jsonString + "Z";

        DateTime datetime = null;
        try {
            datetime = DateTime.parse(jsonString, FMT);
        } catch (Exception ex) {
            //datetime = DateTime.now(DateTimeZone.UTC);
            //throw new JsonParseException("The date time should be a string value");
        }
        return datetime;
    }

    // .NET convert DateTime in ISO8601 format. Use Joda time to convert it.
    private static final DateTimeFormatter FMT = ISODateTimeFormat.dateTime().withZoneUTC();
}

class ByteArrayTypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {

    public JsonElement serialize(byte[] src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(Utils.byteArrayToBase64(src));
    }

    public byte[] deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The byte[] should be a string value");
        }
        return Utils.base64ToByteArray(json.getAsString());
    }
}

public class JsonUtils {
    private static GsonBuilder sBuilder;

    public static String serialize(Object obj) {
        Gson gson = sBuilder.create();
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        Gson gson = sBuilder.create();
        return gson.fromJson(json, clazz);
    }

    public static Object deserialize(String json, Type type) {
        Gson gson = sBuilder.create();
        return gson.fromJson(json, type);
    }

    static {
        sBuilder = new GsonBuilder();
        sBuilder.excludeFieldsWithoutExposeAnnotation();
        sBuilder.registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).create();
        sBuilder.registerTypeAdapter(byte[].class, new ByteArrayTypeAdapter()).create();
    }

}
