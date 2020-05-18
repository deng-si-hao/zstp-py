package com.cavin.culture;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@MapperScan("com.cavin.culture.dao")
@SpringBootApplication
@ServletComponentScan(basePackages = "com.cavin.culture.controller.*")
public class CultureApplication {

    public static void main(String[] args) {
        SpringApplication.run(CultureApplication.class, args);
    }

}
