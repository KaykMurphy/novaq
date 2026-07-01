package com.novaq.mapper;

import com.novaq.dtos.request.ProductUpdateDTO;
import com.novaq.dtos.response.ProductImageResponseDTO;
import com.novaq.dtos.response.ProductResponseDTO;
import com.novaq.dtos.response.ProductVariantResponseDTO;
import com.novaq.model.Product;
import com.novaq.model.ProductImage;
import com.novaq.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDTO toProductResponseDTO(Product product);

    ProductVariantResponseDTO toVariantResponseDTO(ProductVariant variant);

    void updateEntityFromDto(ProductUpdateDTO request, @MappingTarget Product product);

    ProductImageResponseDTO toImageResponseDTO(ProductImage image);



}
