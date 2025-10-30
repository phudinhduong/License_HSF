package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByOrganizationIdAndEmail(UUID orgId, String email);
    @Query("select u from User u where u.organization.code = ?1 and u.email = ?2")
    Optional<User> findByOrgCodeAndEmail(String orgCode, String email);
    //dùng để đăng nhập
    Optional<User> findByEmail(String email);


}
