package com.example.order_service.controllers;

import com.example.order_service.dto.response.ApiResponse;
import com.example.order_service.models.Payment;
import com.example.order_service.services.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService  paymentService;

    @GetMapping("")
    ResponseEntity<ApiResponse<List<Payment>>> getPaymentByUserId(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Integer userId = Integer.valueOf(jwt.getClaimAsString("userId"));
        return paymentService.getPaymentByUserId(userId);
    }

    @GetMapping("/get-all")
    ResponseEntity<ApiResponse<List<Payment>>> getAllPayment(
    ) {
        return paymentService.getAllPayment();
    }
}
