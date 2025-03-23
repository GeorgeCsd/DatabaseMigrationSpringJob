package com.github.george.databasemigration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan({"com.github.george.databasemigration.configuration", "com.github.george.databasemigration.processor",
        "com.github.george.databasemigration.scheduled", "com.github.george.databasemigration.service"
        , "com.github.george.databasemigration.controller"})
@EnableAsync
@EnableScheduling
public class DatabaseMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseMigrationApplication.class, args);
    }

}
