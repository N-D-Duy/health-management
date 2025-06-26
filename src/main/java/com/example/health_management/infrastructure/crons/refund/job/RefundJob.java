package com.example.health_management.infrastructure.crons.refund.job;

import com.example.health_management.infrastructure.crons.refund.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@RequiredArgsConstructor
public class RefundJob implements Job {
    private final RefundService refundService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("------------ RefundJob started ------------");
        try {
            refundService.processRefunds();
        } catch (Exception e) {
            log.error("Error processing refunds: {}", e.getMessage(), e);
            throw new JobExecutionException(e);
        } finally {
            log.info("------------ RefundJob finished ------------");
        }
    }
}
