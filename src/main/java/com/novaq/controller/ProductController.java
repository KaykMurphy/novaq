package com.novaq.controller;

import com.novaq.dtos.request.ProductRequestDTO;
import com.novaq.dtos.response.ProductResponseDTO;
import com.novaq.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid
                                                            ProductRequestDTO request){
        ProductResponseDTO response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
