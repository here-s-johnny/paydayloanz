package com.jg.paydayloanz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value ="file:src/main/resources/config.properties", ignoreResourceNotFound = true)
public class PaydayLoanzApplication {  
    public static void main(String[] args) {  
        SpringApplication.run(PaydayLoanzApplication.class, args);  
    }  
}  
