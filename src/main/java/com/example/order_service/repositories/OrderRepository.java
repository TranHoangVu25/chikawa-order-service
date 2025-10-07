package com.example.order_service.repositories;

import com.example.order_service.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order,String> {
    Optional<Order> findById(String id);

    @Query("{ 'user_id': ?0 }")
    List<Order> findByUserId(Integer userId);

}
