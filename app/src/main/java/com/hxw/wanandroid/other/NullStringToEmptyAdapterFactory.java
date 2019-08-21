package com.hxw.wanandroid.other;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

import static com.hxw.core.utils.DateUtilsKt.string2Date;


/**
 * @author hxw
 * @date 2018/10/10
 */
public class NullStringToEmptyAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType == String.class) {
            return (TypeAdapter<T>) new StringNullAdapter();
        }
        if (rawType == Date.class) {
            return (TypeAdapter<T>) new DateTransformationAdapter();
        }
        return null;
    }

    private class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter writer, String value) throws IOException {

            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }

        @Override
        public String read(JsonReader reader) throws IOException {

            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }


    }

    private class DateTransformationAdapter extends TypeAdapter<Date> {

        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            String d = in.nextString();
            d = d.replace("T", " ").replace("Z", "").substring(0, 19);
            return string2Date(d, "yyyy-MM-dd HH:mm:ss");
        }
    }
}