package ru.stroy1click.attribute.integration;

import org.springframework.boot.SpringApplication;
import ru.stroy1click.attribute.Stroy1ClickAttributeServiceApplication;

public class TestStroy1ClickAttributeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(Stroy1ClickAttributeServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
