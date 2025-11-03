package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LicenseRepository extends JpaRepository<License, UUID> {
    boolean existsByLicenseKey(String licenseKey);
}
