package com.github.george.databasemigration.controller;

import com.github.george.databasemigration.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for triggering data transfer jobs.
 * This controller allows manual job execution via an API call.
 */
@RestController
@RequestMapping("/data-transfer")
public class JobController {

    @Autowired
    JobService jobService;


    /**
     * Manually triggers a batch job for data transfer.
     * The job is started via JobService when this endpoint is called.
     *
     * @param jobName The name of the job to be executed.
     * @return A message confirming the job start request.
     */
    @GetMapping("/trigger")
    public String startJob(@RequestParam String jobName) {
        jobService.startJob();
        return " Job " + jobName + " manually started!";
    }
}
