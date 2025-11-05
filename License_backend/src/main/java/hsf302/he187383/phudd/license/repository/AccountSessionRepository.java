package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface AccountSessionRepository extends JpaRepository<AccountSession, UUID> {
    Optional<AccountSession> findByJti(String jti);
    List<AccountSession> findByAccountId(UUID accountId);
}
