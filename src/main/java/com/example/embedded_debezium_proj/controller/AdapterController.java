package com.example.embedded_debezium_proj.controller;

import com.example.embedded_debezium_proj.adapters.BaseAdapter;
import com.example.embedded_debezium_proj.dto.DBInitialPictureDto;
import com.example.embedded_debezium_proj.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/redis")
public class AdapterController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/get-debezium-data")
    public List<String> getAllDebeziumDataFromRedis(){
       return redisService.getDebeziumDataFromRedis();
    }

    @GetMapping("/get-db-image")
    public ResponseEntity<List<String>> getDBImage(){
        List<String> dbPictureFromRedis = redisService.getDBPictureFromRedis();
        return new ResponseEntity<>(dbPictureFromRedis, HttpStatus.OK);

    }
}
