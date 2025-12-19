package com.example.order_service.services.Impl;

import com.example.order_service.dto.response.ApiResponse;
import com.example.order_service.models.Payment;
import com.example.order_service.repositories.PaymentRepository;
import com.example.order_service.services.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;

    @Override
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentByUserId(Integer userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        if(payments.isEmpty()){
            return ResponseEntity.ok()
                    .body(
                            ApiResponse.<List<Payment>>builder()
                                    .message("Payment list is empty!")
                                    .result(payments)
                                    .build()
                    );
        }
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<Payment>>builder()
                                .message("Get payment list successfully!")
                                .result(payments)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayment() {
        List<Payment> payments = paymentRepository.findAll();
        if(payments.isEmpty()){
            return ResponseEntity.ok()
                    .body(
                            ApiResponse.<List<Payment>>builder()
                                    .message("Payment list is empty!")
                                    .result(payments)
                                    .build()
                    );
        }
        return ResponseEntity.ok()
                .body(
                        ApiResponse.<List<Payment>>builder()
                                .message("Get payment list successfully!")
                                .result(payments)
                                .build()
                );
    }
}
