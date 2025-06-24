package com.example.health_management.infrastructure.crons.refund.jobdetail;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefundJobConfiguration
{
    @Bean(name="refundJobDetail")
    public JobDetail refundJobDetail(){
        return JobBuilder.newJob()
                .ofType(com.example.health_management.infrastructure.crons.refund.job.RefundJob.class)
                .storeDurably()
                .withIdentity("refundJob")
                .withDescription("Refund Job")
                .usingJobData("jobName", "refundJob")
                .build();
    }
}
