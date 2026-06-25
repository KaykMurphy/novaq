package com.novaq.service;

import com.novaq.dtos.request.CartItemRequestDTO;
import com.novaq.dtos.response.CartItemResponseDTO;
import com.novaq.dtos.response.CartResponseDTO;
import com.novaq.model.*;
import com.novaq.repository.CartRepository;
import com.novaq.repository.ProductVariantRepository;
import com.novaq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;

    public CartResponseDTO addItemToCart(CartItemRequestDTO request, String loggedInUserEmail){

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));

        ProductVariant productVariant = productVariantRepository.findById(request.variantId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (productVariant.getStockQuantity() < request.quantity()){
            throw new IllegalArgumentException("Insufficient stock for the requested quantity");
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return newCart;
        });

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(request.variantId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + request.quantity();

            if (productVariant.getStockQuantity() < newQuantity) {
                throw new IllegalArgumentException("Insufficient stock for the desired total quantity");
            }
            existingItem.setQuantity(newQuantity);
    }
        else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductVariant(productVariant);
            newItem.setQuantity(request.quantity());

            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);

        List<CartItemResponseDTO> itemsResponse = savedCart.getItems().stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getProductVariant().getPrice();
                    BigDecimal subTotal = unitPrice.multiply(new BigDecimal(item.getQuantity()));

                    return new CartItemResponseDTO(
                            item.getId(),
                            item.getProductVariant().getId(),
                            item.getProductVariant().getProduct().getName(),
                            item.getProductVariant().getColor(),
                            item.getQuantity(),
                            unitPrice,
                            subTotal
                    );
                }).toList();

        BigDecimal cartTotal = itemsResponse.stream()
                .map(CartItemResponseDTO::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(savedCart.getId(), itemsResponse, cartTotal);
}

    public CartResponseDTO getCart(String loggedInUserEmail) {
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        return mapToResponseDTO(cart);
    }

    public CartResponseDTO removeItemFromCart(UUID itemId, String loggedInUserEmail) {
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));

        if (!removed) {
            throw new IllegalArgumentException("Item not found in this cart");
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToResponseDTO(savedCart);
    }

    public CartResponseDTO clearCart(String loggedInUserEmail) {
        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cart.getItems().clear();

        Cart savedCart = cartRepository.save(cart);
        return mapToResponseDTO(savedCart);
    }

    private CartResponseDTO mapToResponseDTO(Cart cart) {
        List<CartItemResponseDTO> itemsResponse = cart.getItems().stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getProductVariant().getPrice();
                    BigDecimal subTotal = unitPrice.multiply(new BigDecimal(item.getQuantity()));

                    return new CartItemResponseDTO(
                            item.getId(),
                            item.getProductVariant().getId(),
                            item.getProductVariant().getProduct().getName(),
                            item.getProductVariant().getColor(),
                            item.getQuantity(),
                            unitPrice,
                            subTotal
                    );
                }).toList();

        BigDecimal cartTotal = itemsResponse.stream()
                .map(CartItemResponseDTO::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(cart.getId(), itemsResponse, cartTotal);
    }
}
