package com.example.embedded_debezium_proj;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;

import static com.example.embedded_debezium_proj.StructToJsonConverter.convertToJson;

public class DebeziumMessageExtractor {

    public static DebeziumMessage extractFromSourceRecord(SourceRecord sourceRecord) {
        DebeziumMessage debeziumMessage = new DebeziumMessage();

        // Extracting key, value, schema, and table
        Struct keyStruct = (Struct) sourceRecord.key();
        String firstFieldName = keyStruct.schema().fields().get(0).name();

        Struct valueStruct = (Struct) sourceRecord.value();
        Struct after = valueStruct.getStruct("after");
        Struct source = valueStruct.getStruct("source");

        // Set values in DebeziumMessage
        String after_json = convertToJson(after);

        debeziumMessage.setKey(keyStruct.get(firstFieldName).toString());
        debeziumMessage.setValue(after_json);
        debeziumMessage.setSchema(source.getString("schema"));
        debeziumMessage.setTable(source.getString("table"));

        return debeziumMessage;
    }
}
