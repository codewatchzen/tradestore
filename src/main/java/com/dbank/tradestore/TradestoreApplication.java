package com.dbank.tradestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

// Main class only for testing purpose
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dbank.tradestore.repository.sql")
@EnableMongoRepositories(basePackages = "com.dbank.tradestore.repository.nosql")
public class TradestoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradestoreApplication.class, args);
    }
}