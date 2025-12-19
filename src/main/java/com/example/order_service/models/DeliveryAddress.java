package com.example.order_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAddress {

    @Field("recipient_name")
    private String recipientName;

    @Field("phone_number")
    private String phoneNumber;

    private String country;
    private String province;
    private String city;

    @Field("location_detail")
    private String locationDetail;
}

