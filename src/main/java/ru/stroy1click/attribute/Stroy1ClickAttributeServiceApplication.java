package ru.stroy1click.attribute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Stroy1ClickAttributeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Stroy1ClickAttributeServiceApplication.class, args);
    }

}
