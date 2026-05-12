package com.demo.langchain4jspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.demo.langchain4jspringboot.mapper")
public class Langchain4jSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Langchain4jSpringbootApplication.class, args);
    }

}
