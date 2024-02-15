package com.example.embedded_debezium_proj.service.impl;

import com.example.embedded_debezium_proj.adapters.RedisAdapter;
import com.example.embedded_debezium_proj.dto.DBInitialPictureDto;
import com.example.embedded_debezium_proj.dto.DebeziumDto;
import com.example.embedded_debezium_proj.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisAdapter redisAdapter;

    @Override
    public boolean saveDBPictureToRedis(DBInitialPictureDto dbPicture) {
        return redisAdapter.saveDBPictureToRedis(dbPicture);
    }

    @Override
    public List<String> getDBPictureFromRedis() {
        return redisAdapter.getDBPictureFromRedis();
    }

    @Override
    public boolean saveDBChangesToRedis(DebeziumDto debeziumDto) {
        return redisAdapter.saveDBChangesToRedis(debeziumDto);
    }

    @Override
    public List<String> getDebeziumDataFromRedis() {
        return redisAdapter.getDebeziumDataFromRedis();
    }
}
