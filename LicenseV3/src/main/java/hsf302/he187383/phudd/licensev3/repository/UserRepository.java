package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    Boolean existsByEmailIgnoreCase(String email);
}

