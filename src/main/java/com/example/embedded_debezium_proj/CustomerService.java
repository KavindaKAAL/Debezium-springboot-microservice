package com.example.embedded_debezium_proj;

import com.example.embedded_debezium_proj.service.RedisService;
import io.debezium.data.Envelope;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    long startTimeMillis;
    long endTimeMillis;

    List<Long> performance_times = new ArrayList<>();
    long number_of_CRUD_operations = 1000000;
    public void replicateData(Map<String, Object> payload, Envelope.Operation operation) {

//        String s= {group={table_name}, key = {book_id=1},value = {book_id=1,name=amba yaluwo}}

        performance_times.add(1L);

        if (performance_times.size() == 1) {
            startTimeMillis = System.currentTimeMillis();
        }

        if (performance_times.size()>= number_of_CRUD_operations){
            // Print the average of the list contents
//            int total_time =0;
//            for (Long time : performance_times) {
//                total_time+=time;
//
//            }
//            double averageTime = total_time / (double) performance_times.size();
//            System.out.println(String.format("Average Performance Time: %.5f milliseconds", averageTime));
            endTimeMillis = System.currentTimeMillis();
            System.out.println("Diff: " + (endTimeMillis - startTimeMillis));
            System.exit(0);
        }
    }
}
