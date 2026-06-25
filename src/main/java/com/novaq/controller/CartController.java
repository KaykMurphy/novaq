package com.novaq.controller;

import com.novaq.dtos.request.CartItemRequestDTO;
import com.novaq.dtos.response.CartResponseDTO;
import com.novaq.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDTO> addItemToCart(@RequestBody
                                                         @Valid CartItemRequestDTO request,
                                                         @AuthenticationPrincipal UserDetails userDetails){

        String email = userDetails.getUsername();

        CartResponseDTO response = cartService.addItemToCart(request, email);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        CartResponseDTO response = cartService.getCart(email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(
            @PathVariable UUID itemId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        CartResponseDTO response = cartService.removeItemFromCart(itemId, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartResponseDTO> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        CartResponseDTO response = cartService.clearCart(email);
        return ResponseEntity.ok(response);
    }

}
