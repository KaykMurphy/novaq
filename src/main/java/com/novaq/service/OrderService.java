package com.novaq.service;

import com.novaq.dtos.response.CartResponseDTO;
import com.novaq.dtos.response.OrderItemResponseDTO;
import com.novaq.dtos.response.OrderResponseDTO;
import com.novaq.enums.OrderStatus;
import com.novaq.model.*;
import com.novaq.repository.CartRepository;
import com.novaq.repository.OrderRepository;
import com.novaq.repository.ProductVariantRepository;
import com.novaq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;

    public OrderResponseDTO checkout(String emailUsuarioLogado) {

        User user = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Carrinho não encontrado para este usuário"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Não é possível fechar o pedido: o carrinho está vazio.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        BigDecimal totalPedido = BigDecimal.ZERO;

        List<OrderItem> items = new ArrayList<>();

        for (CartItem itemCarrinho : cart.getItems()) {

            ProductVariant variante = itemCarrinho.getProductVariant();

            if (variante.getQuantidadeEstoque() < itemCarrinho.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + variante.getProduto().getNome());
            }

            variante.setQuantidadeEstoque(variante.getQuantidadeEstoque() - itemCarrinho.getQuantidade());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductVariant(variante);
            orderItem.setQuantity(itemCarrinho.getQuantidade());
            orderItem.setPriceAtPurchase(variante.getPreco());

            items.add(orderItem);

            BigDecimal subTotal = variante.getPreco().multiply(new BigDecimal(itemCarrinho.getQuantidade()));

            totalPedido = totalPedido.add(subTotal);
        }

        order.setItems(items);
        order.setTotalAmount(totalPedido);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        List<OrderItemResponseDTO> itensDTO = savedOrder.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getProductVariant().getProduto().getNome(),
                        item.getProductVariant().getSku(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity()))
                )).toList();

        return new OrderResponseDTO(
                savedOrder.getId(),
                savedOrder.getCreatedAt(),
                savedOrder.getStatus(),
                savedOrder.getTotalAmount(),
                itensDTO
        );
    }
}