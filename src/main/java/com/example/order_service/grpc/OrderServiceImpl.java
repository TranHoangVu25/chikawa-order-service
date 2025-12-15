package com.example.order_service.grpc;

// Import các lớp Message độc lập đã được sinh ra
import com.example.grpc.*;
import com.example.order_service.enums.Status;
import com.example.order_service.models.Order;
import com.example.order_service.models.OrderItem;
import com.example.order_service.repositories.OrderRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@GrpcService
@Slf4j
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(
            CartRequest request,
            StreamObserver<OrderResponse> responseObserver
    ) {

        log.info("📦 Received order from user {}", request.getUserId());

        String orderId = UUID.randomUUID().toString();

        List<OrderItem> orderItems = request.getItemsList().stream()
                .map(item -> OrderItem.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .image(item.getImage())
                        .variantId(item.getVariantId())
                        .build())
                .toList();

        Order order = Order.builder()
                .orderId(orderId)
                .userId(request.getUserId())
                .orderItems(orderItems)
                .status(Status.PENDING.name())
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setMessage("Order created successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("✅ Order {} created successfully", orderId);
    }

    // ===================================================
    // PAYMENT → ORDER : Get Snapshot
    // ===================================================
    @Override
    public void getOrderSnapshot(GetOrderRequest request,
                                 StreamObserver<OrderSnapshotResponse> responseObserver) {

        Order order = orderRepository
                .findByOrderIdAndUserId(request.getOrderId(), Integer.parseInt(request.getUserId()))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderSnapshotResponse response = OrderSnapshotResponse.newBuilder()
                .setOrderId(order.getOrderId())
                .setUserId(String.valueOf(order.getUserId()))
//                .setTotalPrice(order.getTotalPrice())
                .setTotalPrice(1000)
                .setSnapshotAt(LocalDateTime.now().toString())
                .addAllItems(order.getOrderItems().stream()
                        .map(i -> com.example.grpc.OrderItem.newBuilder()
                                .setProductId(i.getId())
                                .setName(i.getName())
                                .setPrice(i.getPrice())
                                .setQuantity(i.getQuantity())
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // ===================================================
    // PAYMENT → ORDER : Update Status
    // ===================================================
    @Override
    public void updateOrderStatus(UpdateOrderStatusRequest request,
                                  StreamObserver<UpdateOrderStatusResponse> responseObserver) {

        Order order = orderRepository
                .findByOrderId(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(request.getStatus());
        orderRepository.save(order);

        UpdateOrderStatusResponse response = UpdateOrderStatusResponse.newBuilder()
                .setSuccess(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}