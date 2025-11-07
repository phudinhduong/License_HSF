package hsf302.he187383.phudd.licensev3.service;


import hsf302.he187383.phudd.licensev3.enums.BillingType;
import hsf302.he187383.phudd.licensev3.model.Plan;
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
public class PlanService {

    private final PlanRepository planRepo;
    private final ProductRepository productRepo;

    @Transactional(readOnly = true)
    public Page<Plan> findByProductPaged(UUID productId, int page, int size) {
        size = Math.max(1, Math.min(size, 50));
        page = Math.max(0, page);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "priceCredits"));
        return planRepo.findByProductId(productId, pageable);
    }

    // (nếu cần) bản list cũ:
    @Transactional(readOnly = true)
    public List<Plan> findByProduct(UUID productId) {
        return planRepo.findByProductId(productId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
    }


    @Transactional(readOnly = true)
    public List<Plan> findAll() {
        return planRepo.findAll();
    }

//    @Transactional(readOnly = true)
//    public List<Plan> findByProduct(UUID productId) {
//        return planRepo.findByProductIdOrderByCreatedAtDesc(productId);
//    }

    @Transactional(readOnly = true)
    public Plan findById(UUID id) {
        return planRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
    }

    public Plan create(Plan p) {
        // đảm bảo product hợp lệ
        var product = productRepo.findById(p.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        p.setBillingType(BillingType.SUBSCRIPTION);
        p.setProduct(product);
        return planRepo.save(p);
    }

    public Plan update(UUID id, Plan p) {
        var existing = findById(id);
        existing.setCode(p.getCode());
        existing.setName(p.getName());
        existing.setBillingType(BillingType.SUBSCRIPTION);
        existing.setPriceCredits(p.getPriceCredits());
        existing.setCurrency(p.getCurrency());
        existing.setDurationDays(p.getDurationDays());
        existing.setSeats(p.getSeats());
        existing.setConcurrentLimitPerAccount(p.getConcurrentLimitPerAccount());
        existing.setDeviceLimitPerAccount(p.getDeviceLimitPerAccount());

        if (p.getProduct() != null) {
            var prod = productRepo.findById(p.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            existing.setProduct(prod);
        }

        return planRepo.save(existing);
    }

    public void delete(UUID id) {
        planRepo.deleteById(id);
    }
}

