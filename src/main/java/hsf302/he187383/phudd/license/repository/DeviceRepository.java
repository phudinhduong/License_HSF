package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByOrganizationIdAndFingerprint(UUID orgId, String fingerprint);
}
