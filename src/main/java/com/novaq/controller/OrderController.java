package com.novaq.controller;

import com.novaq.dtos.response.OrderResponseDTO;
import com.novaq.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        OrderResponseDTO response = orderService.checkout(email);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
