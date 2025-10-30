package hsf302.he187383.phudd.license.service;


import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Service
public class LicenseService {

    private final LicenseRepository repo;
    private final OrganizationRepository orgRepo;
    private final ProductRepository productRepo;
    private final PlanRepository planRepo;
    private final LicenseActivationRepository actRepo;
    private final AuditService audit;

    public LicenseService(LicenseRepository repo, OrganizationRepository orgRepo,
                          ProductRepository productRepo, PlanRepository planRepo,
                          LicenseActivationRepository actRepo, AuditService audit) {
        this.repo = repo; this.orgRepo = orgRepo; this.productRepo = productRepo;
        this.planRepo = planRepo; this.actRepo = actRepo; this.audit = audit;
    }

    @Transactional
    public License create(License l, UUID actorId){
        orgRepo.findById(l.getOrganization().getId()).orElseThrow(() -> new ExceptionInInitializerError("Org not found"));
        productRepo.findById(l.getProduct().getId()).orElseThrow(() -> new ExceptionInInitializerError("Product not found"));
        Plan plan = planRepo.findById(l.getPlan().getId()).orElseThrow(() -> new ExceptionInInitializerError("Plan not found"));
        // default limits theo plan nếu chưa set
        if (l.getMaxDevices()==null) l.setMaxDevices(plan.getMaxDevices());
        if (l.getMaxConcurrent()==null) l.setMaxConcurrent(plan.getMaxConcurrent());
        if (l.getType() == null && plan.getType() != null) {
            l.setType(LicenseType.valueOf(plan.getType().name()));
        }
        License saved = repo.save(l);
        audit.log(actorId, l.getOrganization().getId(), "license.created", "LICENSE", saved.getId(), "{}");
        return saved;
    }

    @Transactional
    public License renew(UUID licenseId, int addDays, UUID actorId){
        var lic = repo.findById(licenseId).orElseThrow(() -> new ExceptionInInitializerError("Not found license"));
        if (lic.getStatus() == LicenseStatus.REVOKED) throw new ExceptionInInitializerError("Revoked");
        Instant base = lic.getEndAt() != null ? lic.getEndAt() : Instant.now();
        lic.setEndAt(base.plusSeconds((long) addDays * 86400));
        lic.setStatus(LicenseStatus.ACTIVE);
        audit.log(actorId, lic.getOrganization().getId(), "license.renewed", "LICENSE", lic.getId(), "{\"addDays\":"+addDays+"}");
        return lic;
    }

    @Transactional
    public License changePlan(UUID licenseId, UUID newPlanId, boolean keepLimits, UUID actorId){
        var lic = repo.findById(licenseId).orElseThrow(() -> new ExceptionInInitializerError("Not found license"));
        Plan newPlan = planRepo.findById(newPlanId).orElseThrow(() -> new ExceptionInInitializerError("Not found plan"));
        lic.setPlan(newPlan);
        lic.setType(LicenseType.valueOf(newPlan.getType().name()));
        if (!keepLimits){
            lic.setMaxDevices(newPlan.getMaxDevices());
            lic.setMaxConcurrent(newPlan.getMaxConcurrent());
        }
        audit.log(actorId, lic.getOrganization().getId(), "license.change_plan", "LICENSE", lic.getId(),
                "{\"newPlanId\":\""+newPlanId+"\"}");
        return lic;
    }

    @Transactional
    public License suspend(UUID licenseId, String reason, UUID actorId){
        var lic = repo.findById(licenseId).orElseThrow(() -> new ExceptionInInitializerError("License not found"));
        lic.setStatus(LicenseStatus.SUSPENDED);
        audit.log(actorId, lic.getOrganization().getId(), "license.suspended", "LICENSE", lic.getId(),
                "{\"reason\":\""+(reason==null?"":reason)+"\"}");
        return lic;
    }

    @Transactional
    public License revoke(UUID licenseId, String reason, UUID actorId){
        var lic = repo.findById(licenseId).orElseThrow(() -> new ExceptionInInitializerError("License not found"));
        lic.setStatus(LicenseStatus.REVOKED);
        // deactivate all activations
        actRepo.findByLicenseId(licenseId).forEach(a -> {
            a.setStatus(ActivationStatus.SUSPENDED);
            a.setDeactivatedAt(Instant.now());
        });
        audit.log(actorId, lic.getOrganization().getId(), "license.revoked", "LICENSE", lic.getId(),
                "{\"reason\":\""+(reason==null?"":reason)+"\"}");
        return lic;
    }
}
