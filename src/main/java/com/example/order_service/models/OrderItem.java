package com.example.order_service.models;

import com.example.order_service.validator.PriceConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderItem {
    @Field("product_id")
    String id;

    @Size(message = "INVALID_CART_ITEM_NAME",min = 3)
    String name;

    @PriceConstraint(min = 0)
    @NotNull
    double price;

    @Min(message = "INVALID_QUANTITY",value = 1)
    @NotNull
    int quantity;

    String image;

    String variantId;
}
