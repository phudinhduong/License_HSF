package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final PlanRepository planRepo;

    @Transactional(readOnly = true)
    public List<Order> findByUser(UUID userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Order create(UUID userId, UUID planId, Long priceCredits, String paymentRef) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        var order = Order.builder()
                .user(user)
                .plan(plan)
                .priceCredits(priceCredits)
                .status(OrderStatus.PENDING)
                .paymentRef(paymentRef)
                .build();
        return orderRepo.save(order);
    }

    public void markPaid(UUID orderId) {
        var order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);
    }
}

