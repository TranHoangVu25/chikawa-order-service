package com.example.order_service.services;

import com.example.order_service.dto.response.ApiResponse;
import com.example.order_service.models.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    ResponseEntity<ApiResponse<List<Payment>>> getPaymentByUserId(Integer userId);

    ResponseEntity<ApiResponse<List<Payment>>> getAllPayment();

}
