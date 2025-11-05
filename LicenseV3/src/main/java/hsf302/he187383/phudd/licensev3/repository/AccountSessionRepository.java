package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountSessionRepository extends JpaRepository<AccountSession, UUID> {
    Optional<AccountSession> findByJti(String jti);
    List<AccountSession> findByAccountId(UUID accountId);

    @Query("""
        select count(s) from AccountSession s
         where s.account.id = :accountId
           and s.status = hsf302.he187383.phudd.licensev3.enums.SessionStatus.ACTIVE
           and (s.expiresAt is null or s.expiresAt > :now)
    """)
    long countActiveSessions(@Param("accountId") UUID accountId,
                             @Param("since") Instant since,
                             @Param("now") Instant now);

    @Query("""
    select count(distinct s.deviceId) 
    from AccountSession s
    where s.account.id = :accountId
      and s.deviceId is not null
""")
    long countActivedDevices(@Param("accountId") UUID accountId,
                            @Param("since") Instant since,
                            @Param("now") Instant now);


    @Query("""
        select s from AccountSession s
         where s.account.id = :accountId
           and s.status = hsf302.he187383.phudd.licensev3.enums.SessionStatus.ACTIVE
           and (s.expiresAt is null or s.expiresAt > :now)
         order by coalesce(s.lastSeenAt, s.createdAtSession) asc
    """)
    List<AccountSession> findActiveForPreempt(@Param("accountId") UUID accountId,
                                              @Param("since") Instant since,
                                              @Param("now") Instant now);
}
