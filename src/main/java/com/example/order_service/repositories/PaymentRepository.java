package com.example.order_service.repositories;

import com.example.order_service.models.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment,String> {
    boolean existsByPaymentIntentId(String paymentIntentId);

    List<Payment> findByUserId(Integer userId);
}
