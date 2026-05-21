package com.hellcourt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.hellcourt.mapper")
public class HellCourtApplication {
    public static void main(String[] args) {
        SpringApplication.run(HellCourtApplication.class, args);
    }
}
