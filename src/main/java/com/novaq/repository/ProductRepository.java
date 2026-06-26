package com.novaq.repository;

import com.novaq.model.Category;
import com.novaq.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByCategoryIdAndActiveTrue(UUID categoryId, Pageable pageable);

    Page<Product> findAllByActiveTrue(Pageable pageable);

    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);
}
