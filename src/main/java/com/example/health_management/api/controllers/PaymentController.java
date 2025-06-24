package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderRequest;
import com.example.health_management.application.DTOs.payment.MerchantAppCreateOrderResponse;
import com.example.health_management.application.DTOs.payment.UpdateTransRequest;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.PaymentService;
import com.example.health_management.domain.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final TransactionService transactionService;


    @PostMapping("/payment/zalo/create-order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<MerchantAppCreateOrderResponse>> createZaloPaymentOrder(@RequestBody MerchantAppCreateOrderRequest merchantAppCreateOrderRequest ){
         MerchantAppCreateOrderResponse merchantAppCreateOrderResponse = paymentService.createZaloPaymentOrder(merchantAppCreateOrderRequest);
         ApiResponse<MerchantAppCreateOrderResponse> apiResponse = ApiResponse.<MerchantAppCreateOrderResponse>builder().code(HttpStatus.OK.value()).data(merchantAppCreateOrderResponse).message("Create order successfully").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/payment/zalo/update-order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<String>> updateZaloPaymentOrder(@RequestBody UpdateTransRequest updateOrderRequest) {
        transactionService.updateTransaction(updateOrderRequest);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data("Update order successfully").message("Update order successfully").build();
        return ResponseEntity.ok(apiResponse);
    }
}