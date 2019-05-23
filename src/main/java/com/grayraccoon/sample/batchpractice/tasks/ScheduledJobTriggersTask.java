package com.grayraccoon.sample.batchpractice.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledJobTriggersTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledJobTriggersTask.class);

    @Autowired
    private JobLauncher customJobLauncher;

    @Autowired
    private Job importPeopleFromCSVJob;


    /**
     * Run importPeopleFromCSVJob based on cron expression configured on spring.batch.job.scheduler.cron property.
     *
     * @throws Exception if there is any exception starting or while executing the job.
     */
    @Scheduled(cron = "${spring.batch.job.scheduler.cron}")
    public void importPeopleFromCSVJobTrigger() throws Exception {
        LOGGER.info("Job started at: {}", new Date());

        final JobParameters parameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        final JobExecution execution = customJobLauncher.run( importPeopleFromCSVJob, parameters);

        LOGGER.info("Job finished at: {}, with status: {} ", new Date(), execution.getStatus());
    }

}
