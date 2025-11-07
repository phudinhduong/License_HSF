package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final PlanRepository planRepo;
    private final WalletTxnRepository walletTxnRepo;

    @Transactional(readOnly = true)
    public List<Order> findByUser(UUID userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Order create(UUID userId, UUID planId, Long priceCredits, String paymentRef) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        var order1 = orderRepo.findByUserIdAndPlanId(userId, planId);
        System.out.println(order1);
        if (!order1.isEmpty()) {
            return null;
        }

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


    public Order markPaidWithTxn(UUID orderId, UUID walletTxnId) {
        var order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Lấy reference tới WalletTxn TỒN TẠI trong DB (không new tay)
        var txnRef = walletTxnRepo.getReferenceById(walletTxnId); // JPA proxy, không hit DB ngay

        order.setStatus(OrderStatus.PAID);
        order.setWalletTxn(txnRef);
        return orderRepo.save(order);
    }


    @Transactional(readOnly = true)
    public Page<Order> findMyOrders(UUID userId, int page, int size) {
        size = Math.max(1, Math.min(size, 50));
        page = Math.max(0, page);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepo.findByUserId(userId, pageable);
    }
}

