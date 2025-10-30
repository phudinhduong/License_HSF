package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.*;

import java.util.UUID;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public AppUserDetailsService(UserRepository userRepo) { this.userRepo = userRepo; }

    /**
     * username truyền vào sẽ là: orgId::orgCode::email
     * - orgId có thể là "null"
     * - orgCode có thể là "null"
     */
    @Override
    public UserDetails loadUserByUsername(String composite) throws UsernameNotFoundException {
        String[] parts = composite.split("::", 3);
        if (parts.length != 3) throw new UsernameNotFoundException("Bad login composite");

        UUID orgId = !"null".equals(parts[0]) ? UUID.fromString(parts[0]) : null;
        String orgCode = !"null".equals(parts[1]) ? parts[1] : null;
        String email = parts[2];

        User u = orgId != null
                ? userRepo.findByOrganizationIdAndEmail(orgId, email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found (orgId/email)"))
                : orgCode != null
                ? userRepo.findByOrgCodeAndEmail(orgCode, email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found (orgCode/email)"))
                : userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found (email)"));

        if (u.getStatus() != UserStatus.ACTIVE) throw new UsernameNotFoundException("User disabled");

        String role = "ROLE_" + u.getRole().name(); // ADMIN / ORG_ADMIN / SUPPORT / USER
        return org.springframework.security.core.userdetails.User
                .withUsername(composite)
                .password(u.getPasswordHash())
                .authorities(role)
                .accountLocked(u.getStatus() == UserStatus.LOCKED)
                .disabled(u.getStatus() != UserStatus.ACTIVE)
                .build();
    }
}

