package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface LicenseRepository extends JpaRepository<License, UUID> {
    boolean existsByLicenseKey(String licenseKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from License l where l.id = :id and l.user.id = :userId")
    Optional<License> lockByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
