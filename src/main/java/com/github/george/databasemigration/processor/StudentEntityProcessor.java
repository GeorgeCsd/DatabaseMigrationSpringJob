package com.github.george.databasemigration.processor;

import com.github.george.databasemigration.entity.mysql.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor class for transforming Student data from PostgreSQL to MySQL.
 * This class is used in a Spring Batch job to convert and transfer data
 * between two different database entities.
 */
@Component
public class StudentEntityProcessor implements ItemProcessor<com.github.george.databasemigration.entity.postgresql.Student, Student> {

    private static final Logger logger = LoggerFactory.getLogger(StudentEntityProcessor.class);

    /**
     * Processes a Student entity from PostgreSQL and converts it into
     * a MySQL-compatible Student entity.
     *
     * @param item The Student entity from PostgreSQL.
     * @return A transformed Student entity compatible with MySQL.
     */
    @Override
    public Student process(com.github.george.databasemigration.entity.postgresql.Student item) {
        logger.info("Student id: {}", item.getId());
        Student student = new
                Student();

        student.setId(item.getId());
        student.setFirstName(item.getFirstName());
        student.setLastName(item.getLastName());
        student.setEmail(item.getEmail());
        student.setDeptId(item.getDeptId());
        student.setIsActive(Boolean.parseBoolean(item.getIsActive()));

        return student;

    }

}
