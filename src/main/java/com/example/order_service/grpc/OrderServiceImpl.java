package com.example.order_service.grpc;

// Import các lớp Message độc lập đã được sinh ra
import com.example.grpc.*;
import com.example.order_service.enums.Status;
import com.example.order_service.models.DeliveryAddress;
import com.example.order_service.models.Order;
import com.example.order_service.models.OrderItem;
import com.example.order_service.repositories.OrderRepository;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@GrpcService
@Slf4j
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // =====================================================
    // Cart Service -> Order Service
    // =====================================================
    @Override
    public void createOrder(
            CartRequest request,
            StreamObserver<OrderResponse> responseObserver
    ) {

        log.info("Received order from user {}", request.getUserId());

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

        double totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getPrice() * orderItem.getQuantity();
        }

        DeliveryAddress address =  new DeliveryAddress().builder()
                .recipientName(request.getAddress().getRecipientName())
                .phoneNumber(request.getAddress().getPhoneNumber())
                .country(request.getAddress().getCountry())
                .province(request.getAddress().getProvince())
                .city(request.getAddress().getCity())
                .locationDetail(request.getAddress().getLocationDetail())
                .build();

        Order order = Order.builder()
                .id(orderId)
                .userId(request.getUserId())
                .orderItems(orderItems)
                .status(Status.PENDING.name())
                .confirmedAt(LocalDateTime.now())
                .deliveryAddress(address)
                .build();

        orderRepository.save(order);

        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setMessage("Order created successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("Order {} created successfully", orderId);
    }

    // =====================================================
    // Payment Service -> Order Service
    // =====================================================
    @Override
    public void getOrderSnapshot(
            GetOrderSnapshotRequest request,
            StreamObserver<OrderSnapshotResponse> responseObserver
    ) {

        log.info("Payment requests snapshot for order {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found: " + request.getOrderId())
                );

        // Map OrderItem (entity) -> OrderItem (gRPC)
        List<com.example.grpc.OrderItem> grpcItems =
                order.getOrderItems().stream()
                        .map(item ->
                                com.example.grpc.OrderItem.newBuilder()
                                        .setProductId(item.getId())
                                        .setName(item.getName())
                                        .setPrice(item.getPrice())
                                        .setQuantity(item.getQuantity())
                                        .build()
                        )
                        .toList();

        double totalPrice = 0;
        for (OrderItem item : order.getOrderItems()) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        OrderSnapshotResponse response =
                OrderSnapshotResponse.newBuilder()
                        .setOrderId(order.getId())
                        .setUserId(order.getUserId())
                        .setTotalPrice(totalPrice)
                        .setSnapshotAt(
                                order.getConfirmedAt()
                                        .format(DateTimeFormatter.ISO_DATE_TIME)
                        )
                        .addAllItems(grpcItems)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        log.info("Snapshot for order {} sent to Payment Service", order.getId());
    }
}
