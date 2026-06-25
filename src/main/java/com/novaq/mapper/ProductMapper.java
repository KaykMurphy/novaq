package com.novaq.mapper;

import com.novaq.dtos.response.ProductResponseDTO;
import com.novaq.dtos.response.ProductVariantResponseDTO;
import com.novaq.model.Product;
import com.novaq.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDTO toProductResponseDTO(Product product);

    ProductVariantResponseDTO toVariantResponseDTO(ProductVariant variant);
}
