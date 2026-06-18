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

    public CartResponseDTO addItemToCart(CartItemRequestDTO request, String emailUsuarioLogado){

        User user = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));


        ProductVariant productVariant = productVariantRepository.findById(request.variantId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (productVariant.getQuantidadeEstoque() < request.quantidade()){
            throw new IllegalArgumentException("Estoque insuficiente para a quantidade solicitada.");
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
            int novaQuantidade = existingItem.getQuantidade() + request.quantidade();

            if (productVariant.getQuantidadeEstoque() < novaQuantidade) {
                throw new IllegalArgumentException("Estoque insuficiente para a quantidade total desejada.");
            }
            existingItem.setQuantidade(novaQuantidade);
    }
        else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductVariant(productVariant);
            newItem.setQuantidade(request.quantidade());

            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);

        List<CartItemResponseDTO> itemsResponse = savedCart.getItems().stream()
                .map(item -> {
                    BigDecimal precoUnitario = item.getProductVariant().getPreco();
                    BigDecimal subTotal = precoUnitario.multiply(new BigDecimal(item.getQuantidade()));

                    return new CartItemResponseDTO(
                            item.getId(),
                            item.getProductVariant().getId(),
                            item.getProductVariant().getProduto().getNome(),
                            item.getProductVariant().getCor(),
                            item.getQuantidade(),
                            precoUnitario,
                            subTotal
                    );
                }).toList();

        BigDecimal totalCarrinho = itemsResponse.stream()
                .map(CartItemResponseDTO::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(savedCart.getId(), itemsResponse, totalCarrinho);
}

    public CartResponseDTO getCart(String emailUsuarioLogado) {
        User user = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        return mapToResponseDTO(cart);
    }

    public CartResponseDTO removeItemFromCart(UUID itemId, String emailUsuarioLogado) {
        User user = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));

        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));

        if (!removed) {
            throw new IllegalArgumentException("Item não encontrado neste carrinho");
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToResponseDTO(savedCart);
    }

    public CartResponseDTO clearCart(String emailUsuarioLogado) {
        User user = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado"));

        cart.getItems().clear();

        Cart savedCart = cartRepository.save(cart);
        return mapToResponseDTO(savedCart);
    }

    private CartResponseDTO mapToResponseDTO(Cart cart) {
        List<CartItemResponseDTO> itemsResponse = cart.getItems().stream()
                .map(item -> {
                    BigDecimal precoUnitario = item.getProductVariant().getPreco();
                    BigDecimal subTotal = precoUnitario.multiply(new BigDecimal(item.getQuantidade()));

                    return new CartItemResponseDTO(
                            item.getId(),
                            item.getProductVariant().getId(),
                            item.getProductVariant().getProduto().getNome(),
                            item.getProductVariant().getCor(),
                            item.getQuantidade(),
                            precoUnitario,
                            subTotal
                    );
                }).toList();

        BigDecimal totalCarrinho = itemsResponse.stream()
                .map(CartItemResponseDTO::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(cart.getId(), itemsResponse, totalCarrinho);
    }
}
