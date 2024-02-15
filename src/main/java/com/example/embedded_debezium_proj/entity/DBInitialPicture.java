package com.example.embedded_debezium_proj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DBInitialPicture {

        private long id;
        private String key;
        private String dbImage;

}
