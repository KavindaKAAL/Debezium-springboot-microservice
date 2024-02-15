package com.example.embedded_debezium_proj.adapters;

import com.example.embedded_debezium_proj.dto.DBInitialPictureDto;
import com.example.embedded_debezium_proj.dto.DebeziumDto;

import java.util.List;

public interface BaseAdapter <T> {

    boolean saveDBPictureToRedis(DBInitialPictureDto dbPicture);
    List<String> getDBPictureFromRedis();

    boolean saveDBChangesToRedis(DebeziumDto<T> debeziumDto);

    List<String> getDebeziumDataFromRedis();

}
