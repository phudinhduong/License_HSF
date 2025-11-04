package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByUsernameIgnoreCase(String username);
    long countByLicenseId(UUID licenseId);
    Page<Account> findByLicenseId(UUID licenseId, Pageable pageable);
}
