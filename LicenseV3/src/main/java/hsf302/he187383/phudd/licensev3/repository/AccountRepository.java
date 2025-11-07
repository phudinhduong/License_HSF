package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);
    List<Account> findByLicenseId(UUID licenseId);

    @EntityGraph(attributePaths = {})
    Page<Account> findByLicenseId(UUID licenseId, org.springframework.data.domain.Pageable pageable);
}
