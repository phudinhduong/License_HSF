package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LicenseRepository extends JpaRepository<License, UUID> {
    Optional<License> findByLicenseKey(String licenseKey);
    List<License> findByOrganizationId(UUID orgId);
    @Query("select count(l) from License l where l.endAt < ?1 and l.status='ACTIVE'")
    long countActiveExpiredBefore(Instant now);
}
