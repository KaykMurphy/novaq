package com.novaq.controller;

import com.novaq.dtos.request.ProductRequestDTO;
import com.novaq.dtos.request.ProductUpdateDTO;
import com.novaq.dtos.response.ProductResponseDTO;
import com.novaq.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO request) {
        ProductResponseDTO response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable UUID id) {
        ProductResponseDTO response = productService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid ProductUpdateDTO request){

        ProductResponseDTO response = productService.updateProduct(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(@PageableDefault(page = 0, size = 12, sort = "name")
                                                                Pageable pageable) {
        Page<ProductResponseDTO> responsePage = productService.findAll(pageable);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> findByCategory(@PathVariable UUID categoryId,
                                                                   @PageableDefault(page = 0, size = 12) Pageable pageable) {
        Page<ProductResponseDTO> responsePage = productService.findByCategory(categoryId, pageable);
        return ResponseEntity.ok(responsePage);
    }



}
