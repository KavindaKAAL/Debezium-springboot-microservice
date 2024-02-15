package com.example.embedded_debezium_proj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.kafka.connect.data.Struct;

public class StructToJsonConverter {

    public static String convertToJson(Struct struct) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Register the custom serializer
            SimpleModule module = new SimpleModule();
            module.addSerializer(Struct.class, new StructSerializer());
            objectMapper.registerModule(module);

            return objectMapper.writeValueAsString(struct);
        } catch (Exception e) {
            // Handle the exception according to your application's requirements
            e.printStackTrace();
            return null;
        }
    }

}

