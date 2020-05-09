package com.neo.paymodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.neo.paymodel.api.pay.*")
public class PayModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayModelApplication.class, args);
    }

}
