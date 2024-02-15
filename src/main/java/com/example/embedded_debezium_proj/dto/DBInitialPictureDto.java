package com.example.embedded_debezium_proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;


@Data

@NoArgsConstructor
@AllArgsConstructor
@Service
public class DBInitialPictureDto implements Serializable {
//    private String key;
    private String dbImage;
}
