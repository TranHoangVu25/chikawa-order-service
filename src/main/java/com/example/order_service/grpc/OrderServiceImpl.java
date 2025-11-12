package com.example.order_service.grpc;

// Import các lớp Message độc lập đã được sinh ra
import com.example.grpc.CartRequest;
import com.example.grpc.OrderResponse;
import com.example.grpc.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    @Override
    public void createOrder(CartRequest request,
                            StreamObserver<OrderResponse> responseObserver) {

        // 1. Xử lý logic tạo đơn hàng
        String orderId = UUID.randomUUID().toString();

        // Dùng phương thức getter tự động sinh ra trong CartRequest
        System.out.println("✅ Received order from user " + request.getUserId());
        System.out.println("Items received: " + request.getItemsList().size());

        // 2. Tạo phản hồi (OrderResponse)
        // Dùng phương thức newBuilder() của lớp OrderResponse
        OrderResponse response = OrderResponse.newBuilder()
                .setOrderId(orderId)
                .setMessage("Order created successfully for user " + request.getUserId())
                .build();

        // 3. Gửi phản hồi và kết thúc
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}