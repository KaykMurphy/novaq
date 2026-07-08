package com.novaq.service;

import com.novaq.dtos.request.ProductRequestDTO;
import com.novaq.dtos.request.ProductUpdateDTO;
import com.novaq.dtos.response.ProductResponseDTO;
import com.novaq.mapper.ProductMapper;
import com.novaq.model.Category;
import com.novaq.model.Product;
import com.novaq.model.ProductImage;
import com.novaq.model.ProductVariant;
import com.novaq.repository.CategoryRepository;
import com.novaq.repository.ProductRepository;
import com.novaq.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrand(request.brand());
        product.setCategory(category);
        product.setImageUrl(request.imageUrl());
        product.setVariations(new ArrayList<>());
        product.setImages(new ArrayList<>());

        if (request.images() != null) {
            for (var imageDto : request.images()) {
                ProductImage image = new ProductImage();
                image.setUrl(imageDto.url());
                image.setPosition(imageDto.position());
                image.setProduct(product);
                product.getImages().add(image);
            }
        }

        Set<String> skusVistos = new HashSet<>();

        for (var variantDto : request.variations()) {

            if (!skusVistos.add(variantDto.sku().toUpperCase())) {
                throw new IllegalArgumentException("SKU '" + variantDto.sku() + "' is duplicated in the request");
            }

            var existingSku = productVariantRepository.findBySku(variantDto.sku());

            if (existingSku.isPresent() && existingSku.get().isActive()) {
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

    @Transactional
    public void deleteProduct(UUID productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setActive(false);

        for (ProductVariant v : product.getVariations()){
            v.setActive(false);
        }

        productRepository.save(product);
    }

    public ProductResponseDTO updateProduct(UUID productId, ProductUpdateDTO request){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productMapper.updateEntityFromDto(request, product);

        Product product1 = productRepository.save(product);

        return productMapper.toProductResponseDTO(product1);

    }

    public Page<ProductResponseDTO> findAll(Pageable pageable) {
        Page<Product> pageOfProducts = productRepository.findAllByActiveTrue(pageable);
        return pageOfProducts.map(productMapper::toProductResponseDTO);
    }

    public Page<ProductResponseDTO> findByCategory(UUID categoryId, Pageable pageable) {
        Page<Product> pageOfProducts = productRepository.findByCategoryId(categoryId, pageable);
        return pageOfProducts.map(productMapper::toProductResponseDTO);
    }


    public ProductResponseDTO findById(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!product.isActive()) {
            throw new IllegalArgumentException("Product not found");
        }

        return productMapper.toProductResponseDTO(product);
    }
}
