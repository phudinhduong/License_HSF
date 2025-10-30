package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.model.AuditLog;
import hsf302.he187383.phudd.license.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class AuditService {
    private final AuditLogRepository repo;

    public AuditService(AuditLogRepository repo){ this.repo = repo; }

    @Transactional
    public void log(UUID actorId, UUID orgId, String action, String entityType, UUID entityId, String detailsJson){
        AuditLog log = AuditLog.builder()
                .actorId(actorId)
                .orgId(orgId)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(detailsJson == null ? "{}" : detailsJson)
                .build();
        repo.save(log);
    }
}
