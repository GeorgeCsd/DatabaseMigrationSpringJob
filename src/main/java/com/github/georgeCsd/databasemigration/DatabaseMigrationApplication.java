package com.github.georgeCsd.databasemigration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan({"com.github.georgeCsd.databasemigration.configuration", "com.github.georgeCsd.databasemigration.processor",
        "com.github.georgeCsd.databasemigration.scheduled", "com.github.georgeCsd.databasemigration.service"
        , "com.github.georgeCsd.databasemigration.controller"})
@EnableAsync
@EnableScheduling
public class DatabaseMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseMigrationApplication.class, args);
    }

}
