package com.example.embedded_debezium_proj;

import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.storage.OffsetBackingStore;
import org.apache.kafka.connect.util.Callback;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class CustomOffsetBackingStore implements OffsetBackingStore {

    private String fileName = "C:/Users/ANSHAN/Downloads/demo1/offsetspg.dat";
    private Map<ByteBuffer, ByteBuffer> offsetStore = new HashMap<>();

    @Override
    public void start() {
        // Initialization logic if needed
    }

    @Override
    public void stop() {
        // Cleanup logic if needed
    }

    @Override
    public Future<Map<ByteBuffer, ByteBuffer>> get(Collection<ByteBuffer> keys) {
        CompletableFuture<Map<ByteBuffer, ByteBuffer>> future = new CompletableFuture<>();
        Map<ByteBuffer, ByteBuffer> result = new HashMap<>();

        try {
            // Open the .dat file for reading
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
                // Read the serializedOffsets map
                Map<byte[], byte[]> serializedOffsets = (Map<byte[], byte[]>) ois.readObject();


                // Convert byte[] keys and values to ByteBuffer
                for (Map.Entry<byte[], byte[]> entry : serializedOffsets.entrySet()) {
                    ByteBuffer keyBuffer = ByteBuffer.wrap(entry.getKey());
                    ByteBuffer valueBuffer = ByteBuffer.wrap(entry.getValue());

                    // Check if the key is in the requested keys
                    if (keys.contains(keyBuffer)) {
                        result.put(keyBuffer, valueBuffer);
                    }
                }
            }

            future.complete(result);
        } catch (IOException | ClassNotFoundException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public Future<Void> set(Map<ByteBuffer, ByteBuffer> offsets, Callback<Void> callback) {
        Map<byte[], byte[]> serializedOffsets = new HashMap<>();

        for (Map.Entry<ByteBuffer, ByteBuffer> entry : offsets.entrySet()) {
            ByteBuffer key = entry.getKey();
            ByteBuffer value = entry.getValue();

            byte[] keyBytes = new byte[key.remaining()];
            key.get(keyBytes);

            byte[] valueBytes = new byte[value.remaining()];
            value.get(valueBytes);

            serializedOffsets.put(keyBytes, valueBytes);

        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(serializedOffsets);
        } catch (IOException e) {
            e.printStackTrace();
        }

        callback.onCompletion(null, null);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Set<Map<String, Object>> connectorPartitions(String s) {
        return null;
    }

    @Override
    public void configure(WorkerConfig workerConfig) {
        // Configuration logic if needed
    }
}
