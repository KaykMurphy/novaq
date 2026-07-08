package com.novaq.repository;

import com.novaq.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByPaymentId(String paymentId);

    Page<Order> findAll(Pageable pageable);

}
