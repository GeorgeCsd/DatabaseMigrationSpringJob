package com.github.georgeCsd.databasemigration.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


import com.github.georgeCsd.databasemigration.entity.postgresql.Student;

/**
 * Processor class for transforming Student data from PostgreSQL to MySQL.
 * This class is used in a Spring Batch job to convert and transfer data
 * between two different database entities.
 */
@Component
public class DataTransferProcessor implements ItemProcessor<Student, com.github.georgeCsd.databasemigration.entity.mysql.Student> {

    /**
     * Processes a Student entity from PostgreSQL and converts it into
     * a MySQL-compatible Student entity.
     *
     * @param item The Student entity from PostgreSQL.
     * @return A transformed Student entity compatible with MySQL.
     * @throws Exception If any processing error occurs.
     */
    @Override
    public com.github.georgeCsd.databasemigration.entity.mysql.Student process(Student item) throws Exception {
        System.out.println("Student id: " + item.getId());
        com.github.georgeCsd.databasemigration.entity.mysql.Student student = new
                com.github.georgeCsd.databasemigration.entity.mysql.Student();

        student.setId(item.getId());
        student.setFirstName(item.getFirstName());
        student.setLastName(item.getLastName());
        student.setEmail(item.getEmail());
        student.setDeptId(item.getDeptId());
        student.setIsActive(item.getIsActive() != null ?
                Boolean.valueOf(item.getIsActive()) : false);

        return student;

    }

}
