package com.github.george.databasemigration.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job dataTransferingJob;

    @Mock
    private JobExecution jobExecution;

    @InjectMocks
    private JobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartJob() throws Exception {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));

        when(jobLauncher.run(eq(dataTransferingJob), any(JobParameters.class))).thenReturn(jobExecution);
        when(jobExecution.getId()).thenReturn(1L);

        jobService.startJob();

        verify(jobLauncher, times(1)).run(eq(dataTransferingJob), any(JobParameters.class));
    }

    @Test
    void testStartJob_ExceptionHandling() throws Exception {
        when(jobLauncher.run(eq(dataTransferingJob), any(JobParameters.class))).thenThrow(new RuntimeException("Job failed"));

        jobService.startJob();

        verify(jobLauncher, times(1)).run(eq(dataTransferingJob), any(JobParameters.class));
    }
}
