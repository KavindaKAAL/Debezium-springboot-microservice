package com.example.embedded_debezium_proj;

import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.debezium.data.Envelope.FieldName.*;

@Component
public class DebeziumListener {
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private final CustomerService customerService;

    public DebeziumListener(Configuration customerConnectorConfiguration, CustomerService customerService) {
        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(customerConnectorConfiguration.asProperties())
                .notifying(record->{System.out.println(record);})
                .build();

        this.customerService = customerService;
    }
//this::handleChangeEvent
    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {

        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        Struct sourceRecordChangeValue = (Struct) sourceRecord.value();

        if (sourceRecordChangeValue != null) {
//            System.out.println(sourceRecordChangeValue);
            Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

            if (operation != Envelope.Operation.READ) {
                String record = operation == Envelope.Operation.DELETE ? BEFORE : AFTER;
                Struct struct = (Struct) sourceRecordChangeValue.get(record);
                Map<String, Object> payload = struct.schema().fields().stream()
                        .filter(field -> struct.get(field) != null)
                        .collect(Collectors.toMap(org.apache.kafka.connect.data.Field::name, field -> struct.get(field)));



                // Extract ts_ms values
//                long tsMsBefore = sourceRecordChangeValue.getStruct("source").getInt64("ts_ms");
//                long tsMsAfter = sourceRecordChangeValue.getInt64("ts_ms");

                // Calculate time difference in milliseconds
//                long timeDifference = tsMsAfter - tsMsBefore;
                this.customerService.replicateData(payload, operation);

            }
        }
    }

    private final Executor executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }
}
