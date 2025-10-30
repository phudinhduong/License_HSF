package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByLicenseId(UUID licenseId);
}
