package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WebhookRepository extends JpaRepository<Webhook, UUID> {
    List<Webhook> findByOrganizationIdOrOrganizationIsNullAndIsActiveTrue(UUID orgId);
}
