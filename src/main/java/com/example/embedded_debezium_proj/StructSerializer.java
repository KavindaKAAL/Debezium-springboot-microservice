package com.example.embedded_debezium_proj;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.kafka.connect.data.Struct;

import java.io.IOException;

public class StructSerializer extends JsonSerializer<Struct> {

    @Override
    public void serialize(Struct struct, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        // Iterate over the fields in the struct and serialize them
        struct.schema().fields().forEach(field -> {
            try {
                jsonGenerator.writeFieldName(field.name());
                jsonGenerator.writeObject(struct.get(field));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jsonGenerator.writeEndObject();
    }
}

