package com.example.embedded_debezium_proj.service;


import com.example.embedded_debezium_proj.dto.DBInitialPictureDto;
import com.example.embedded_debezium_proj.dto.DebeziumDto;

import java.util.List;

public interface RedisService {
    public boolean saveDBPictureToRedis(DBInitialPictureDto dbPicture);

    public List<String> getDBPictureFromRedis();

    public boolean saveDBChangesToRedis(DebeziumDto debeziumDto);

    public List<String> getDebeziumDataFromRedis();
}
