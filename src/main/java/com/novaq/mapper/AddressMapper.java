package com.novaq.mapper;

import com.novaq.dtos.request.CheckoutRequestDTO;
import com.novaq.dtos.response.ViaCepResponseDTO;
import com.novaq.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)

    @Mapping(source = "request.cep", target = "cep")
    @Mapping(source = "request.complemento", target = "complemento")
    Address toEntity (ViaCepResponseDTO postalCode, CheckoutRequestDTO request);

}
