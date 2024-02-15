package com.example.embedded_debezium_proj.adapters;

import com.example.embedded_debezium_proj.dto.DBInitialPictureDto;
import com.example.embedded_debezium_proj.dto.DebeziumDto;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisAdapter implements BaseAdapter{

    @Autowired
    private RedisTemplate redisTemplate;

    public static String DataBaseKey;
    private static final String DebeziumKey = "DEBEZIUM-KEY";


    @Override
    public boolean saveDBPictureToRedis(DBInitialPictureDto dbPicture) {
        try {

            String val = dbPicture.getDbImage();
            ValueOperations<String,String> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(DataBaseKey,val);
           // redisTemplate.opsForHash().put(DataBaseKey,dbPicture.getKey(),dbPicture.getDbImage());

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getDBPictureFromRedis() {
        return redisTemplate.opsForHash().values(DataBaseKey);
    }

    @Override
    public boolean saveDBChangesToRedis(DebeziumDto debeziumDto) {
        return false;
    }

    @Override
    public List<String> getDebeziumDataFromRedis() {
        return null;
    }
}
