package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.enums.ActivationStatus;
import hsf302.he187383.phudd.license.model.LicenseActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LicenseActivationRepository extends JpaRepository<LicenseActivation, UUID> {
    Optional<LicenseActivation> findByLicenseIdAndDeviceId(UUID licenseId, UUID deviceId);
    long countByLicenseIdAndStatus(UUID licenseId, ActivationStatus status);
    @Query("select count(a) from LicenseActivation a where a.license.id=?1 and a.status='ACTIVE'")
    long countActiveByLicenseId(UUID licenseId);
    List<LicenseActivation> findByLicenseId(UUID licenseId);
}
