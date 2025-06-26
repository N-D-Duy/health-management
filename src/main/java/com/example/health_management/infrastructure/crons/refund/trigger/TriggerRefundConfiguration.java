package com.example.health_management.infrastructure.crons.refund.trigger;

import com.example.health_management.infrastructure.crons.config.JobFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.quartz.Trigger;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TriggerRefundConfiguration {
    private final JobFactory jobFactory;

    @Bean
    public Trigger triggerRefundJob(@Qualifier("refundJobDetail") JobDetail refundJobDetail) {
        log.info("Creating trigger for Refund Job");
        try{
           String jobName = "AUTO_REFUND";
           return TriggerBuilder.newTrigger()
                   .forJob(refundJobDetail)
                   .withIdentity(jobName)
                   .withDescription("Trigger for Refund Job")
                   .withSchedule(CronScheduleBuilder.cronSchedule(jobFactory.getCronJobByName(jobName)))
                   .build();
        } catch (Exception e) {
            log.error("Error creating trigger for Refund Job: {}", e.getMessage(), e);
            return null;
        }
    }
}
