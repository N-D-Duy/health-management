package com.example.health_management.common.utils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    @AfterReturning("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void logAfterTransactionSuccess() {
        logger.info("Transaction completed successfully");
    }
}