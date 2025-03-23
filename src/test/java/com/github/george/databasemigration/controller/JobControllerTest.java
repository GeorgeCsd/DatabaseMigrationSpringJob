package com.github.george.databasemigration.controller;

import static org.mockito.Mockito.*;


import com.github.george.databasemigration.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class JobControllerTest {

    @Mock
    private JobService jobService;


    @InjectMocks
    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartJob() {
        String jobName = "dataTransferJob";

        String response = jobController.startJob(jobName);

        assertEquals(" Job " + jobName + " manually started!", response);
        verify(jobService, times(1)).startJob();
    }
}