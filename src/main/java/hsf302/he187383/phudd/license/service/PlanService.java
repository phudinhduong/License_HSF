package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.model.Plan;
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PlanService {
    private final PlanRepository repo;
    private final ProductRepository productRepo;

    public PlanService(PlanRepository repo, ProductRepository productRepo) {
        this.repo = repo; this.productRepo = productRepo;
    }

    public List<Plan> listByProduct(UUID productId){ return repo.findByProductId(productId); }

    @Transactional
    public Plan create(Plan plan){
        // đảm bảo product tồn tại
        productRepo.findById(plan.getProduct().getId())
                .orElseThrow(() -> new ExceptionInInitializerError("Product not found"));
        return repo.save(plan);
    }

    @Transactional
    public Plan update(UUID id, Plan patch){
        var e = repo.findById(id).orElseThrow(() -> new ExceptionInInitializerError("Plan not found"));
        if (patch.getName()!=null) e.setName(patch.getName());
        if (patch.getType()!=null) e.setType(patch.getType());
        if (patch.getDurationDays()!=null) e.setDurationDays(patch.getDurationDays());
        if (patch.getMaxDevices()!=null) e.setMaxDevices(patch.getMaxDevices());
        if (patch.getMaxConcurrent()!=null) e.setMaxConcurrent(patch.getMaxConcurrent());
        if (patch.getFeatures()!=null) e.setFeatures(patch.getFeatures());
        if (patch.getIsActive()!=null) e.setIsActive(patch.getIsActive());
        return e;
    }
}
