package com.kurcsinorbert.webshop_processor.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;

    private final Job importUserJob;

    @Override
    public void run(String... args) throws Exception {
        JobExecution jobExecution = jobLauncher.run(importUserJob, new JobParameters());
        System.out.println("Job Execution Status: " + jobExecution.getStatus());
    }
}
