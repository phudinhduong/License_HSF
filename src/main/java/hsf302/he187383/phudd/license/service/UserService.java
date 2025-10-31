package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.UserStatus;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repo;
    private final OrganizationRepository orgRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, OrganizationRepository orgRepo, PasswordEncoder encoder){
        this.repo = repo; this.orgRepo = orgRepo; this.encoder = encoder;
    }
    @Transactional
    public User create(User u){
        orgRepo.findById(u.getOrganization().getId()).orElseThrow(() -> new ExceptionInInitializerError("Org not found"));
        u.setPasswordHash(encoder.encode(u.getPasswordHash())); // mapper đã map password -> passwordHash tạm
        u.setStatus(UserStatus.ACTIVE);
        return repo.save(u);
    }

    @Transactional(readOnly = true)
    public User authenticate(UUID orgId, String email, String rawPassword){
        var u = repo.findByOrganizationIdAndEmail(orgId, email)
                .orElseThrow(() -> new ExceptionInInitializerError("User not found"));
        if (!encoder.matches(rawPassword, u.getPasswordHash()))
            throw new ExceptionInInitializerError("Invalid credentials");
        return u;
    }
}
