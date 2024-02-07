package com.example.embedded_debezium_proj;

import io.debezium.connector.postgresql.PostgresConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebeziumConnector {
    @Autowired
    private CustomOffsetBackingStore customOffsetBackingStore;

    @Value("${source.datasource.hostname}")
    private String customerDbHost;

    @Value("${source.datasource.username}")
    private String customerDbUsername;

    @Value("${source.datasource.password}")
    private String customerDbPassword;

    @Value("${source.datasource.dbname}")
    private String customerDbName;
    @Value("${source.datasource.port}")
    private String customerDbPort;


    @Bean
    public io.debezium.config.Configuration customerConnector() {
        return io.debezium.config.Configuration.create()
                .with("connector.class", PostgresConnector.class.getName())
                .with("plugin.name", "pgoutput")
                .with("offset.storage",  customOffsetBackingStore.getClass().getName())
//                .with("offset.storage",  "org.apache.kafka.connect.storage.FileOffsetBackingStore")
//                .with("offset.storage.file.filename", "C:/Users/ANSHAN/Downloads/demo1/offsetspg.dat")
                .with("offset.flush.interval.ms", "6000")
                .with("name", "postgres-connector")
                .with("database.hostname", "localhost")
                .with("database.port", "5432")
                .with("database.user", "postgres")
                .with("database.password", "admin123")
                .with("database.dbname", "customerdb_source")
                .with("include.schema.changes", "false")
                .with("topic.prefix", "kfk")
                .with("snapshot.mode", "always")
                .build();
}}
