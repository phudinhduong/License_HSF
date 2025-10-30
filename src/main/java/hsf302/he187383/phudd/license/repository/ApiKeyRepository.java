package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
    Optional<ApiKey> findByOrganizationIdAndName(UUID orgId, String name);
}
