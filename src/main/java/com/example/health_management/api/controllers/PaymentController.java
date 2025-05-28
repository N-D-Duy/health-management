package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderRequest;
import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderResponse;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/zalo/create-order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<MerchantAppCreateOrderResponse>> createZaloPaymentOrder(@RequestBody MerchantAppCreateOrderRequest merchantAppCreateOrderRequest ){
         MerchantAppCreateOrderResponse merchantAppCreateOrderResponse = paymentService.createZaloPaymentOrder(merchantAppCreateOrderRequest);
         ApiResponse<MerchantAppCreateOrderResponse> apiResponse = ApiResponse.<MerchantAppCreateOrderResponse>builder().code(HttpStatus.OK.value()).data(merchantAppCreateOrderResponse).message("Create order successfully").build();
        return ResponseEntity.ok(apiResponse);
    }
}
