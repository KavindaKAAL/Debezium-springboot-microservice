package com.example.embedded_debezium_proj;

import com.example.embedded_debezium_proj.adapters.RedisAdapter;
import com.example.embedded_debezium_proj.dto.DBInitialPictureDto;
import com.example.embedded_debezium_proj.service.RedisService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.debezium.data.Envelope.FieldName.*;

@Component
public class DebeziumListener {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DBInitialPictureDto dbInitialPictureDto;

    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private final CustomerService customerService;

    public DebeziumListener(Configuration customerConnectorConfiguration, CustomerService customerService) {
        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(customerConnectorConfiguration.asProperties())
                .notifying(this::handleChangeEvent)
                .build();

        this.customerService = customerService;
    }
//    record->{System.out.println(record);}
//    this::handleChangeEvent
    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {

        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        System.out.println(sourceRecord);
//        Struct sourceRecordChangeValue = (Struct) sourceRecord.value();
//        System.out.println(sourceRecordChangeValue);

        // Extracting information from the SourceRecord
        DebeziumMessage debeziumMessage = DebeziumMessageExtractor.extractFromSourceRecord(sourceRecord);

        // use debeziumMessage.getKey(), debeziumMessage.getValue(), debeziumMessage.getSchema(), and debeziumMessage.getTable() as needed
        System.out.println("Key: " + debeziumMessage.getKey());
        System.out.println("Value: " + debeziumMessage.getValue());
        System.out.println("Schema: " + debeziumMessage.getSchema());
        System.out.println("Table: " + debeziumMessage.getTable());

        // save data to Redis
        RedisAdapter.DataBaseKey = debeziumMessage.getSchema()+"_"+ debeziumMessage.getTable()+"_"+debeziumMessage.getKey();
//        dbInitialPictureDto.setKey(debeziumMessage.getKey());
        dbInitialPictureDto.setDbImage(debeziumMessage.getValue());

        redisService.saveDBPictureToRedis(dbInitialPictureDto);


//        if (sourceRecordChangeValue != null) {
////            System.out.println(sourceRecordChangeValue);
//            Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));
//
//            if (operation != Envelope.Operation.READ) {
//                String record = operation == Envelope.Operation.DELETE ? BEFORE : AFTER;
//                Struct struct = (Struct) sourceRecordChangeValue.get(record);
//                Map<String, Object> payload = struct.schema().fields().stream()
//                        .filter(field -> struct.get(field) != null)
//                        .collect(Collectors.toMap(org.apache.kafka.connect.data.Field::name, field -> struct.get(field)));
//
//
//
//                // Extract ts_ms values
////                long tsMsBefore = sourceRecordChangeValue.getStruct("source").getInt64("ts_ms");
////                long tsMsAfter = sourceRecordChangeValue.getInt64("ts_ms");
//
//                // Calculate time difference in milliseconds
////                long timeDifference = tsMsAfter - tsMsBefore;
////                this.customerService.replicateData(payload, operation);
//
//            }
//        }
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
