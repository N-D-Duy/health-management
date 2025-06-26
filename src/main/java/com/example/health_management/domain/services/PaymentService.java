package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.payment.*;
import com.example.health_management.common.Constants;
import com.example.health_management.common.shared.enums.ZaloPayRefundStatus;
import com.example.health_management.common.utils.zalopay.h_mac.ZaloPayHelper;
import com.example.health_management.domain.entities.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public MerchantAppCreateOrderResponse createZaloPaymentOrder(MerchantAppCreateOrderRequest request) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(org.springframework.http.HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        ZaloPayOrderResponse zaloPayOrderResponse;
        try {
            ZaloPayOrderRequest zaloPayOrderRequest = ZaloPayOrderRequest.builder().amount(request.getAmount()).appUser(request.getUserId().toString()).build();
            zaloPayOrderRequest.setItem("[]");
            zaloPayOrderRequest.setAppId(Constants.APP_ID);
            zaloPayOrderRequest.setEmbedData("{}");
            zaloPayOrderRequest.setAppTransId(ZaloPayHelper.getAppTransId());
            zaloPayOrderRequest.setBankCode("zalopayapp");
            zaloPayOrderRequest.setDescription(request.getDescription());
            zaloPayOrderRequest.setAppTime(new Date().getTime());
            zaloPayOrderRequest.getMacWithHelper();

            String zaloPayOrderRequestJson = objectMapper.writeValueAsString(zaloPayOrderRequest);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(zaloPayOrderRequestJson, headers);
            zaloPayOrderResponse = objectMapper.readValue(
                    restTemplate.postForObject(Constants.ZALO_PAY_SERVER_SAND_BOX_BASE_URL + "/create", entity, String.class),
                    ZaloPayOrderResponse.class
            );

            transactionService.createTransaction(zaloPayOrderResponse.getZpTransToken(), request.getAmount());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("MAC_KEY invalid");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request");
        }
        return MerchantAppCreateOrderResponse.builder().zpTransToken(zaloPayOrderResponse.getZpTransToken()).build();
    }

    public void refundAppointmentTransaction(Long appointmentId, double refundRate) {
        Transaction transaction = transactionService.findTransactionByAppointmentId(appointmentId);
        if (transaction == null) {
            throw new IllegalArgumentException("Completed transaction not found for the given appointment ID");
        }

        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(org.springframework.http.HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        ZaloPayRefundRequest zaloPayRefundRequest = new ZaloPayRefundRequest();
        try {
            zaloPayRefundRequest.setAppId(Constants.APP_ID);
            zaloPayRefundRequest.setZpTransId(transaction.getTransactionId());
            zaloPayRefundRequest.setAmount((long) (transaction.getAmount() * refundRate));
            zaloPayRefundRequest.setMRefundId(ZaloPayHelper.getMRefundId(Constants.APP_ID));
            zaloPayRefundRequest.setDescription("Refund for appointment ID: " + appointmentId);
            zaloPayRefundRequest.setTimestamp(new Date().getTime());
            zaloPayRefundRequest.setMacWithHelper();

            String zaloPayRefundRequestJson = objectMapper.writeValueAsString(zaloPayRefundRequest);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(zaloPayRefundRequestJson, headers);
            ZaloPayRefundResponse zaloPayRefundResponse = objectMapper.readValue(
                    restTemplate.postForObject(Constants.ZALO_PAY_SERVER_SAND_BOX_BASE_URL + "/refund", entity, String.class),
                    ZaloPayRefundResponse.class
            );
            transactionService.updateMRefundId(appointmentId, zaloPayRefundRequest.getMRefundId());

            log.info("Refund response: {}", zaloPayRefundResponse);

            // if success, update transaction status, notify user
//            transactionService.updateTransactionStatus(transaction.getTransactionId(), AppointmentTransactionStatus.REFUNDED);

        } catch (InvalidKeyException e) {
            throw new RuntimeException("MAC_KEY invalid");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize request");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ZaloPayRefundStatus queryRefundStatus(Long appointmentId) throws Exception {
        Transaction transaction = transactionService.findTransactionByAppointmentId(appointmentId);
        if (transaction == null) {
            throw new IllegalArgumentException("Completed transaction not found for the given appointment ID");
        }

        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(org.springframework.http.HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        ZaloPayQueryRefundStatusRequest zaloPayQueryRefundStatusRequest =  new ZaloPayQueryRefundStatusRequest();

        zaloPayQueryRefundStatusRequest.setAppId(Constants.APP_ID);
        zaloPayQueryRefundStatusRequest.setMRefundId(transaction.getMRefundId());
        zaloPayQueryRefundStatusRequest.setTimestamp(System.currentTimeMillis());
        zaloPayQueryRefundStatusRequest.setMacWithHelper();

        String zaloPayQueryRefundStatusRequestJson = objectMapper.writeValueAsString(zaloPayQueryRefundStatusRequest);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(zaloPayQueryRefundStatusRequestJson, headers);
        ZaloPayQueryRefundStatusResponse zaloPayQueryRefundStatusResponse = objectMapper.readValue(
                restTemplate.postForObject(Constants.ZALO_PAY_SERVER_SAND_BOX_BASE_URL + "/query_refund", entity, String.class),
                ZaloPayQueryRefundStatusResponse.class
        );
        log.info("Query refund status response: {}", zaloPayQueryRefundStatusResponse);
        return ZaloPayRefundStatus.fromCode(zaloPayQueryRefundStatusResponse.getReturnCode());
    }
}
