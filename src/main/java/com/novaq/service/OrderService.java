package com.novaq.service;

import com.mercadopago.MercadoPagoConfig;
import com.novaq.dtos.request.CheckoutRequestDTO;
import com.novaq.dtos.request.MercadoPagoWebhookDTO;
import com.novaq.dtos.request.PixPaymentDTO;
import com.novaq.dtos.response.OrderItemResponseDTO;
import com.novaq.dtos.response.OrderResponseDTO;
import com.novaq.dtos.response.PixResponseDTO;
import com.novaq.dtos.response.ViaCepResponseDTO;
import com.novaq.enums.OrderStatus;
import com.novaq.exceptions.WebhookProcessingException;
import com.novaq.mapper.AddressMapper;
import com.novaq.mapper.OrderMapper;
import com.novaq.mapper.ProductMapper;
import com.novaq.model.*;
import com.novaq.repository.CartRepository;
import com.novaq.repository.OrderRepository;
import com.novaq.repository.ProductVariantRepository;
import com.novaq.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final ViaCepService viaCepService;
    private final AddressMapper addressMapper;
    private final MercadoPagoService mercadoPagoService;
    private final OrderMapper orderMapper;


    @Transactional
    public OrderResponseDTO checkout(String loggedInUserEmail, CheckoutRequestDTO request) {

        User user = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for this user"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout: cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING_PAYMENT);


        ViaCepResponseDTO response = viaCepService.findAddressByPostalCode(request.cep());

        Address address = addressMapper.toEntity(response, request);
        address.setOrder(order);

        order.setAddress(address);


        BigDecimal orderTotal = BigDecimal.ZERO;

        List<OrderItem> items = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {

            ProductVariant variant = cartItem.getProductVariant();

            if (variant.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + variant.getProduct().getName());
            }

            variant.setStockQuantity(variant.getStockQuantity() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductVariant(variant);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(variant.getPrice());

            items.add(orderItem);

            BigDecimal subTotal = variant.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));

            orderTotal = orderTotal.add(subTotal);
        }

        order.setItems(items);
        order.setTotalAmount(orderTotal);

        Order savedOrder = orderRepository.save(order);

        PixPaymentDTO pixPaymentDTO = new PixPaymentDTO();
        pixPaymentDTO.setAmount(savedOrder.getTotalAmount());
        pixPaymentDTO.setOrderId(savedOrder.getId().toString());
        pixPaymentDTO.setEmail(user.getEmail());
        pixPaymentDTO.setFirstName(user.getFullName());
        pixPaymentDTO.setLastName("");
        pixPaymentDTO.setCpf(request.cpf());

        PixResponseDTO pixResponse;
        try {
            pixResponse = mercadoPagoService.createPixPayment(pixPaymentDTO);
        } catch (Exception e) {
            log.error("Falha ao criar pagamento PIX para pedido {}: {}", savedOrder.getId(), e.getMessage());
            throw new RuntimeException("Erro ao processar pagamento. Tente novamente.", e);
        }

        savedOrder.setPaymentId(pixResponse.paymentId().toString());
        savedOrder.setQrCodePix(pixResponse.qrCodeCopyPaste());
        savedOrder.setQrCodeBase64(pixResponse.qrCodeBase64());

        orderRepository.save(savedOrder);

        cart.getItems().clear();
        cartRepository.save(cart);

        List<OrderItemResponseDTO> itemsDTO = savedOrder.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getProductVariant().getProduct().getName(),
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
                itemsDTO,
                savedOrder.getQrCodePix(),
                savedOrder.getQrCodeBase64(),
                savedOrder.getUser().getId(),
                savedOrder.getUser().getFullName(),
                savedOrder.getPaymentId()
        );
    }


    @Transactional
    public void processPaymentNotification(MercadoPagoWebhookDTO payload){

        if (payload == null || payload.data() == null || payload.data().id() == null){
            throw new WebhookProcessingException("Invalid or incomplete webhook payload");
        }

        String paymentId = payload.data().id();

        Order order = orderRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID){
            return;
        }

        order.setStatus(OrderStatus.PAID);

        orderRepository.save(order);
    }

    
    public Page<OrderResponseDTO> findAll(Pageable pageable) {
        Page<Order> pageOfOrders = orderRepository.findAll(pageable);
        return pageOfOrders.map(orderMapper::toOrderResponseDTO);
    }

}
