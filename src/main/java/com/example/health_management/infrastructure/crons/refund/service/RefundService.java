package com.example.health_management.infrastructure.crons.refund.service;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.DepositStatus;
import com.example.health_management.common.shared.enums.ZaloPayRefundStatus;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.repositories.AppointmentRecordRepository;
import com.example.health_management.domain.services.AppointmentRecordService;
import com.example.health_management.domain.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {
    private final AppointmentRecordRepository appointmentRecordRepository;
    private final PaymentService paymentService;
    private final AppointmentRecordService appointmentRecordService;

    public void processRefunds() throws Exception {
        log.info("Processing refunds for appointments...");
        log.info("Current time: {}", java.time.LocalDateTime.now());
        List<AppointmentRecord> targetAppointmentList =  appointmentRecordRepository.findByAppointmentStatusAndListOfDepositStatus(AppointmentStatus.CANCELLED, List.of(
                DepositStatus.FULL_REFUND_PENDING,
                DepositStatus.PARTIAL_REFUND_PENDING
        ));

        if (targetAppointmentList.isEmpty()) {
            log.info("No appointments found for refund processing.");
            return;
        }

        for( AppointmentRecord appointment : targetAppointmentList) {
            log.info("Processing refund for appointment ID: {}", appointment.getId());
            ZaloPayRefundStatus appointmentRefundStatus = paymentService.queryRefundStatus(appointment.getId());
            if (appointmentRefundStatus == ZaloPayRefundStatus.SUCCESS) {
                log.info("Refund for appointment ID {} is already successful.", appointment.getId());
                appointmentRecordService.updateDepositStatus(appointment.getId(), DepositStatus.REFUNDED);
            } else if (appointmentRefundStatus == ZaloPayRefundStatus.PENDING) {
                log.info("Refund for appointment ID {} is still pending.", appointment.getId());
            } else if (appointmentRefundStatus == ZaloPayRefundStatus.FAILED) {
                log.info("Retrying refund for appointment ID: {}", appointment.getId());
                paymentService.refundAppointmentTransaction(appointment.getId(), appointment.getDepositStatus().getRefundRate());
            } else {
                log.warn("Unknown refund status for appointment ID: {}", appointment.getId());
            }
        }
        log.info("Refund service is working correctly!");
    }
}
