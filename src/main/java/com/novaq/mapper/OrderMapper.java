package com.novaq.mapper;

import com.novaq.dtos.response.OrderItemResponseDTO;
import com.novaq.dtos.response.OrderResponseDTO;
import com.novaq.model.Order;
import com.novaq.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    @Mapping(source = "totalAmount", target = "totalValue")
    @Mapping(source = "items", target = "items")
    OrderResponseDTO toOrderResponseDTO(Order order);

    @Mapping(source = "productVariant.product.name", target = "productName")
    @Mapping(source = "productVariant.sku", target = "sku")
    @Mapping(source = "priceAtPurchase", target = "purchasePrice")
    OrderItemResponseDTO toOrderItemResponseDTO(OrderItem item);
}