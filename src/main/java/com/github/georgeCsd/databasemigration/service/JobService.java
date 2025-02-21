package com.github.georgeCsd.databasemigration.service;


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

    @Autowired
    JobLauncher jobLauncher;

    @Qualifier("dataTransferingJob")
    @Autowired
    Job dataTransferingJob;

    /**
     * Starts the batch job asynchronously.
     * It sets job parameters, including the current timestamp, and triggers the execution.
     */
    @Async
    public void startJob() {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);

        try {
            JobExecution jobExecution = jobLauncher.run(dataTransferingJob, jobParameters);
            System.out.println("Job Execution ID = " + jobExecution.getId());
        } catch (Exception e) {
            System.out.println("Exception while starting job");
        }
    }
}
