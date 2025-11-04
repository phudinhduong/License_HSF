package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.dto.plan.*;
import hsf302.he187383.phudd.license.dto.plan.PlanResp;
import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.mapper.JpaRefResolver;
import hsf302.he187383.phudd.license.mapper.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository repo;
    private final PlanMapper mapper;
    private final JpaRefResolver ref;

    public PlanResp create(PlanCreateReq req) {
        // Unique: code per product
        if (repo.existsByProductIdAndCodeIgnoreCase(req.getProductId(), req.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Plan code already exists for this product");
        }

        validateBusiness(req.getBillingType(), req.getDurationDays());

        Plan entity = mapper.toEntity(req, ref);

        // Normalize currency default
        if (entity.getCurrency() == null || entity.getCurrency().isBlank()) {
            entity.setCurrency("USD");
        }

        // PERPETUAL => durationDays = null
        if (entity.getBillingType() == BillingType.PERPETUAL) {
            entity.setDurationDays(null);
        }

        entity = repo.save(entity);
        return mapper.toResp(entity);
    }

    public PlanResp update(UUID id, PlanUpdateReq req) {
        Plan entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));

        // Unique with possibly changed productId
        if (repo.existsByProductIdAndCodeIgnoreCaseAndIdNot(req.getProductId(), req.getCode(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Plan code already exists for this product");
        }

        validateBusiness(req.getBillingType(), req.getDurationDays());

        mapper.update(entity, req, ref);

        // Normalize currency default
        if (entity.getCurrency() == null || entity.getCurrency().isBlank()) {
            entity.setCurrency("USD");
        }

        if (entity.getBillingType() == BillingType.PERPETUAL) {
            entity.setDurationDays(null);
        }

        entity = repo.save(entity);
        return mapper.toResp(entity);
    }

    public void delete(UUID id) {
        Plan entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        try {
            repo.delete(entity);
        } catch (DataIntegrityViolationException ex) {
            // Đang bị Order/License tham chiếu
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Plan is referenced by other records");
        }
    }

    public PlanResp get(UUID id) {
        return repo.findById(id)
                .map(mapper::toResp)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
    }

    public Page<PlanResp> list(UUID productId, String q, Pageable pageable) {
        String qq = (q == null || q.isBlank()) ? null : q.trim();
        Page<Plan> page = repo.search(productId, qq, pageable);
        return page.map(mapper::toResp);
    }

    private void validateBusiness(BillingType billingType, Integer durationDays) {
        if (billingType == BillingType.SUBSCRIPTION) {
            if (durationDays == null || durationDays <= 0) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "SUBSCRIPTION requires durationDays > 0");
            }
        } else if (billingType == BillingType.PERPETUAL) {
            // allow null/0; service sẽ normalize thành null
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown billing type");
        }
    }
}
