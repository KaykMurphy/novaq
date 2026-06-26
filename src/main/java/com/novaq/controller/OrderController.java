package com.novaq.controller;

import com.novaq.dtos.request.CheckoutRequestDTO;
import com.novaq.dtos.response.OrderResponseDTO;
import com.novaq.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(
            @RequestBody @Valid CheckoutRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        OrderResponseDTO response = orderService.checkout(email, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
