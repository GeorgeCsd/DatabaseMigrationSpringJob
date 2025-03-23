package com.github.george.databasemigration.scheduled;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.github.george.databasemigration.processor.DataTransferProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import com.github.george.databasemigration.entity.postgresql.Student;

/**
 * Configuration class for defining a Spring Batch job that transfers
 * student data from a PostgreSQL database to a MySQL database.
 */
@Configuration
public class DataTransferJob {

    private final StepBuilderFactory stepBuilderFactory;

    private final DataTransferProcessor dataTransferProcessor;

    private final  DataSource universitydatasource;

    private final  DataSource postgresdatasource;

    private final EntityManagerFactory postgresqlEntityManagerFactory;

    private final EntityManagerFactory mysqlEntityManagerFactory;

    private final JpaTransactionManager jpaTransactionManager;

    @Autowired
    public DataTransferJob(StepBuilderFactory stepBuilderFactory,DataTransferProcessor dataTransferProcessor,@Qualifier("universitydatasource") DataSource universitydatasource,@Qualifier("postgresdatasource")
    DataSource postgresdatasource,@Qualifier("postgresqlEntityManagerFactory") EntityManagerFactory postgresqlEntityManagerFactory,@Qualifier("mysqlEntityManagerFactory") EntityManagerFactory mysqlEntityManagerFactory,JpaTransactionManager jpaTransactionManager) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataTransferProcessor = dataTransferProcessor;
        this.universitydatasource = universitydatasource;
        this.postgresdatasource = postgresdatasource;
        this.postgresqlEntityManagerFactory = postgresqlEntityManagerFactory;
        this.mysqlEntityManagerFactory = mysqlEntityManagerFactory;
        this.jpaTransactionManager = jpaTransactionManager;
    }

    /**
     * Defines the batch job for transferring student data.
     *
     * @return A configured Job instance.
     */
    @Bean
    public Job dataTransferingJob(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("Data Transfering Job")
                .incrementer(new RunIdIncrementer())
                .start(firstDataTransferingStep())
                .build();
    }

    /**
     * Defines the step for transferring data.
     * This step reads student data from PostgreSQL, processes it,
     * and writes it to MySQL.
     *
     * @return A configured Step instance.
     */
    private Step firstDataTransferingStep() {
        return stepBuilderFactory.get("First Data Transfering Step")
                .<Student, com.github.george.databasemigration.entity.mysql.Student>chunk(5000)
                .reader(jpaCursorItemReader())
                .processor(dataTransferProcessor)
                .writer(jpaItemWriter())
                .faultTolerant()
                .skip(Throwable.class)
                .skipLimit(100)
                .retryLimit(3)
                .retry(Throwable.class)
                .transactionManager(jpaTransactionManager)
                .build();
    }

    /**
     * Configures an item reader to fetch student records from PostgreSQL using JPA.
     *
     * @return A JpaCursorItemReader for reading students.
     */
    public JpaCursorItemReader<Student> jpaCursorItemReader() {
        JpaCursorItemReader<Student> jpaCursorItemReader = new JpaCursorItemReader<>();
        jpaCursorItemReader.setEntityManagerFactory(postgresqlEntityManagerFactory);
        jpaCursorItemReader.setQueryString("From Student");
        return jpaCursorItemReader;
    }

    /**
     * Configures an item writer to save student records into MySQL using JPA.
     *
     * @return A JpaItemWriter for writing students.
     */
    public JpaItemWriter<com.github.george.databasemigration.entity.mysql.Student> jpaItemWriter() {
        JpaItemWriter<com.github.george.databasemigration.entity.mysql.Student> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(mysqlEntityManagerFactory);
        return jpaItemWriter;
    }

}
