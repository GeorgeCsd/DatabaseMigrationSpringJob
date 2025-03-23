package com.github.george.databasemigration.processor;

import com.github.george.databasemigration.entity.postgresql.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataTransferProcessorTest {

    private DataTransferProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new DataTransferProcessor();
    }

    @Test
    void testProcess() {
        Student postgresStudent = new Student();
        postgresStudent.setId(1L);
        postgresStudent.setFirstName("John");
        postgresStudent.setLastName("Doe");
        postgresStudent.setEmail("john.doe@example.com");
        postgresStudent.setDeptId(10L);
        postgresStudent.setIsActive("true");

        com.github.george.databasemigration.entity.mysql.Student mysqlStudent = processor.process(postgresStudent);

        assertNotNull(mysqlStudent);
        assertEquals(postgresStudent.getId(), mysqlStudent.getId());
        assertEquals(postgresStudent.getFirstName(), mysqlStudent.getFirstName());
        assertEquals(postgresStudent.getLastName(), mysqlStudent.getLastName());
        assertEquals(postgresStudent.getEmail(), mysqlStudent.getEmail());
        assertEquals(postgresStudent.getDeptId(), mysqlStudent.getDeptId());
        assertTrue(mysqlStudent.getIsActive());
    }

    @Test
    void testProcess_NullIsActive() {
        Student postgresStudent = new Student();
        postgresStudent.setId(2L);
        postgresStudent.setFirstName("Jane");
        postgresStudent.setLastName("Smith");
        postgresStudent.setEmail("jane.smith@example.com");
        postgresStudent.setDeptId(20L);
        postgresStudent.setIsActive(null);

        com.github.george.databasemigration.entity.mysql.Student mysqlStudent = processor.process(postgresStudent);

        assertNotNull(mysqlStudent);
        assertFalse(mysqlStudent.getIsActive());
    }
}