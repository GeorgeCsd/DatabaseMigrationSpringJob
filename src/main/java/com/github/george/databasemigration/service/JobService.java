package com.github.george.databasemigration.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class responsible for managing the execution of batch jobs.
 * It provides methods to start a job asynchronously.
 */
@Service
public class JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("dataTransferingJob")
    @Autowired
    Job dataTransferingJob;

    @Qualifier("letterShiftingJob")
    @Autowired
    Job letterShiftingJob;

    /**
     * Starts the batch job asynchronously.
     * It sets job parameters, including the current timestamp, and triggers the execution.
     */
    @Async
    public void startJob(String jobName) {
        
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);
        logger.info("Starting job {}", jobName);
        try {
            
            if (jobName.equals("dataTransferingJob")) {
                JobExecution jobExecution = jobLauncher.run(dataTransferingJob, jobParameters);
                logger.info("Job Execution ID = {}", jobExecution.getId());
            } 
            else if (jobName.equals("letterShiftingJob")) {
                JobExecution jobExecution = jobLauncher.run(letterShiftingJob, jobParameters);
                logger.info("Job Execution ID = {}", jobExecution.getId());
            }
            
        } catch (Exception e) {
            logger.error("Exception while starting job", e);
        }
    }
}
