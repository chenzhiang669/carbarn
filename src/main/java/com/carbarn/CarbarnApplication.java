package com.carbarn;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScans({@MapperScan("com.carbarn.im.mapper"), @MapperScan("com.carbarn.inter.mapper")})
@ComponentScan("com.carbarn.*")
public class CarbarnApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarbarnApplication.class, args);
    }
}
