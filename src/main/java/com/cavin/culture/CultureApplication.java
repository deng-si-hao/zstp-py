package com.cavin.culture;


import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;



@SpringBootApplication
@ServletComponentScan(basePackages = "com.cavin.culture.controller.*")
@MapperScan({"com.cavin.culture.dao","com.cavin.culture.neo4jdao"})
public class CultureApplication {

    public static void main(String[] args) {
        SpringApplication.run(CultureApplication.class, args);
    }


}
