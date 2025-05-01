package com.github.george.databasemigration.scheduled;

import com.github.george.databasemigration.entity.mysql.NameEntry;
import com.github.george.databasemigration.processor.StudentNameProcessor;
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

import javax.persistence.EntityManagerFactory;

/**
 * Configuration class for defining a Spring Batch job that shifts by two
 * letters in student names from a PostgreSQL database and stores them to
 * a MySQL database.
 */
@Configuration
public class LetterShiftJob {
    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory postgresqlEntityManagerFactory;

    private final EntityManagerFactory nameEntityManagerFactory;

    private final JpaTransactionManager studentNameTransactionManager;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StudentNameProcessor studentNameProcessor;

    @Autowired
    public LetterShiftJob(StepBuilderFactory stepBuilderFactory, EntityManagerFactory postgresqlEntityManagerFactory, @Qualifier("nameEntityManagerFactory") EntityManagerFactory nameEntityManagerFactory, @Qualifier("studentNameTransactionManager") JpaTransactionManager studentNameTransactionManager) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.postgresqlEntityManagerFactory = postgresqlEntityManagerFactory;
        this.nameEntityManagerFactory = nameEntityManagerFactory;
        this.studentNameTransactionManager = studentNameTransactionManager;
    }

    /**
     * Defines the Spring Batch Job for transforming student names by shifting letters.
     * This job uses a RunIdIncrementer to allow re-execution with different parameters.
     *
     * @return The configured Job instance named "LetterShiftJob".
     */
    @Bean
    public Job letterShiftingJob() {
        return jobBuilderFactory.get("LetterShifting Job")
                .incrementer(new RunIdIncrementer())
                .start(transformNamesStep())
                .build();
    }

    /**
     * Defines the step for transforming student names by shifting each character.
     * This step reads student first names from the PostgreSQL database, processes each name
     * by shifting letters, and writes the transformed names to the target database.
     * Fault tolerance is enabled to skip or retry on exceptions.
     *
     * @return Configured Step instance.
     */
    private Step transformNamesStep() {
        return stepBuilderFactory.get("transformNames Step")
                .<String, NameEntry>chunk(5000)
                .reader(studentNameReader())
                .processor(studentNameProcessor)
                .writer(studentNameWriter())
                .faultTolerant()
                .skip(Throwable.class)
                .skipLimit(100)
                .retryLimit(3)
                .retry(Throwable.class)
                .transactionManager(studentNameTransactionManager)
                .build();
    }

    /**
     * Configures a JPA cursor item reader to fetch student first names from the PostgreSQL database.
     *
     * @return A configured {@link JpaCursorItemReader} that returns student first names as Strings.
     */
    public JpaCursorItemReader<String> studentNameReader() {
        JpaCursorItemReader<String> jpaCursorItemReader = new JpaCursorItemReader<>();
        jpaCursorItemReader.setEntityManagerFactory(postgresqlEntityManagerFactory);
        jpaCursorItemReader.setQueryString("SELECT s.firstName FROM Student s");
        return jpaCursorItemReader;
    }

    /**
     * Configures a JPA item writer to write transformed name entries to the target database.
     *
     * @return A configured {@link JpaItemWriter} for {@link NameEntry} entities.
     */
    public JpaItemWriter<NameEntry> studentNameWriter() {
        JpaItemWriter<NameEntry> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(nameEntityManagerFactory);
        return jpaItemWriter;
    }
}