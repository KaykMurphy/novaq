package com.novaq.service;

import com.novaq.dtos.request.ProductRequestDTO;
import com.novaq.dtos.response.ProductResponseDTO;
import com.novaq.mapper.ProductMapper;
import com.novaq.model.Category;
import com.novaq.model.Product;
import com.novaq.model.ProductVariant;
import com.novaq.repository.CategoryRepository;
import com.novaq.repository.ProductRepository;
import com.novaq.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setCategory(category);
        product.setVariations(new ArrayList<>());

        for (var variantDto : request.variations()) {

            var existingSku = productVariantRepository.findBySku(variantDto.sku());

            if (existingSku.isPresent()) {
                throw new IllegalArgumentException("SKU '" + variantDto.sku() + "' already exists in the system");
            }

            ProductVariant variant = new ProductVariant();
            variant.setSku(variantDto.sku());
            variant.setColor(variantDto.color());
            variant.setStockQuantity(variantDto.stockQuantity());
            variant.setPrice(variantDto.price());
            variant.setProduct(product);

            product.getVariations().add(variant);
        }

        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDTO(savedProduct);
    }

    public Page<ProductResponseDTO> findAll(Pageable pageable) {
        Page<Product> pageOfProducts = productRepository.findAll(pageable);
        return pageOfProducts.map(productMapper::toProductResponseDTO);
    }


    public Page<ProductResponseDTO> findByCategory(UUID categoryId, Pageable pageable) {
        Page<Product> pageOfProducts = productRepository.findByCategoryId(categoryId, pageable);
        return pageOfProducts.map(productMapper::toProductResponseDTO);
    }
}
