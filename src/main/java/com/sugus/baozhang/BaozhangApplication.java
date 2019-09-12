package com.sugus.baozhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.sugus.baozhang.*.repository")
public class BaozhangApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaozhangApplication.class, args);
    }

}
