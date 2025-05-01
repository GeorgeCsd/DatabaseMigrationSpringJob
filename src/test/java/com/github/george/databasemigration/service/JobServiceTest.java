package com.github.george.databasemigration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;


import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job dataTransferingJob;

    @Mock
    private Job letterShiftingJob;

    @Mock
    private JobExecution jobExecution;

    @InjectMocks
    private JobService jobService;

    @Captor
    private ArgumentCaptor<JobParameters> jobParametersCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartDataTransferingJob() throws Exception {
        when(jobLauncher.run(eq(dataTransferingJob), any())).thenReturn(jobExecution);
        when(jobExecution.getId()).thenReturn(1L);

        jobService.startJob("dataTransferingJob");

        verify(jobLauncher).run(eq(dataTransferingJob), jobParametersCaptor.capture());
        verify(jobExecution).getId();
    }

    @Test
    void testStartLetterShiftJob() throws Exception {
        when(jobLauncher.run(eq(letterShiftingJob), any())).thenReturn(jobExecution);
        when(jobExecution.getId()).thenReturn(2L);

        jobService.startJob("letterShiftingJob");

        verify(jobLauncher).run(eq(letterShiftingJob), jobParametersCaptor.capture());
        verify(jobExecution).getId();
    }

    @Test
    void testStartUnknownJobDoesNothing() throws Exception {
        jobService.startJob("unknownJob");

        verify(jobLauncher, never()).run(any(), any());
    }

    @Test
    void testStartJobThrowsException() throws Exception {
        when(jobLauncher.run(eq(dataTransferingJob), any())).thenThrow(new RuntimeException("Failed"));

        jobService.startJob("dataTransferingJob");

        verify(jobLauncher).run(eq(dataTransferingJob), any());
    }
}