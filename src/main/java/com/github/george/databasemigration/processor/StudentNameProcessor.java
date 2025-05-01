package com.github.george.databasemigration.processor;

import com.github.george.databasemigration.entity.mysql.NameEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Processor class for transforming student names.
 * This class is used in a Spring Batch job to convert plain string names
 * into encrypted {@link NameEntry} objects by shifting each character forward by 2.
 */
@Component
public class StudentNameProcessor implements ItemProcessor<String, NameEntry> {
    private static final Logger logger = LoggerFactory.getLogger(StudentNameProcessor.class);

    /**
     * Processes a student name by transforming each character in the string.
     * Each character is shifted forward by 2 positions in the Unicode character set.
     * The transformed string is then set in a {@link NameEntry} object.
     *
     * @param name The original student name as a String.
     * @return A {@link NameEntry} object containing the transformed name.
     */
    @Override
    public NameEntry process(String name) {
        NameEntry nameEntry = new NameEntry();
        StringBuilder transformedWord = new StringBuilder();

        for (char c : name.toCharArray()) {
            char newChar = (char) (c + 2);  // Move each letter forward by 2
            transformedWord.append(newChar);
        }

        logger.info("name {} transformed to {}", name, transformedWord);
        nameEntry.setName(transformedWord.toString());
        return nameEntry;
    }
}
