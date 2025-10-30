package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.KeyPairEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface KeyPairRepository extends JpaRepository<KeyPairEntity, UUID> {
    @Query("select k from KeyPairEntity k where k.activeFrom <= ?1 and (k.activeTo is null or k.activeTo >= ?1) order by k.activeFrom desc")
    Optional<KeyPairEntity> findCurrent(Instant now);
    Optional<KeyPairEntity> findByKid(String kid);
}
