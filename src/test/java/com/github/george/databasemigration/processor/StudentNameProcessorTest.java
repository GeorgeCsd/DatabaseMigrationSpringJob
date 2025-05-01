package com.github.george.databasemigration.processor;

import com.github.george.databasemigration.entity.mysql.NameEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentNameProcessorTest {

    private final StudentNameProcessor processor = new StudentNameProcessor();

    @Test
    void testProcessSimpleName() {
        String input = "Alice";
        NameEntry result = processor.process(input);

        String expected = "Cnkeg";
        assertNotNull(result);
        assertEquals(expected, result.getName());
    }

    @Test
    void testProcessEmptyString() throws NullPointerException {
        String input = "";
        NameEntry result = processor.process(input);

        assertNotNull(result);
        assertEquals("", result.getName());
    }

    @Test
    void testProcessSpecialCharacters() {
        String input = "Zz!9";
        NameEntry result = processor.process(input);

        // Z -> \
        // z -> |
        // ! -> #
        // 9 -> ;
        String expected = "\\|#;";
        assertEquals(expected, result.getName());
    }

    @Test
    void testProcessNullInput() {
        assertThrows(NullPointerException.class, () -> {
            processor.process(null);
        });
    }
}