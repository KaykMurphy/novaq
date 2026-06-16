package com.novaq.controller;

import com.novaq.dtos.request.CartItemRequestDTO;
import com.novaq.dtos.response.CartItemResponseDTO;
import com.novaq.dtos.response.CartResponseDTO;
import com.novaq.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDTO> addItemToCart(@RequestBody
                                                         @Valid CartItemRequestDTO request,
                                                         @AuthenticationPrincipal UserDetails userDetails){

        //email user
        String email = userDetails.getUsername();

        CartResponseDTO response = cartService.addItemToCart(request, email);

        return ResponseEntity.ok(response);

    }

}
