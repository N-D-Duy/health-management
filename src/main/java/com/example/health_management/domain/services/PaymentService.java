package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderRequest;
import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderResponse;
import com.example.health_management.application.DTOs.payment.ZaloPayOrderRequest;
import com.example.health_management.application.DTOs.payment.ZaloPayOrderResponse;
import com.example.health_management.common.Constants;
import com.example.health_management.common.utils.zalopay.h_mac.ZaloPayHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;

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
            ZaloPayOrderResponse zaloPayOrderResponse = null;
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

}
