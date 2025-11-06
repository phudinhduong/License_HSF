package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.enums.UserStatus;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final WalletRepository walletRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerNewUser(String email, String rawPassword) {
        if (userRepo.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("email_exists");
        }
        var u = User.builder()
                .email(email.trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();
        u = userRepo.save(u);

        walletRepo.save(Wallet.builder()
                .user(u)
                .balance(0L)
                .status(WalletStatus.ACTIVE)
                .build());

        return u;
    }


    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User create(User user) {
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }
        return userRepo.save(user);
    }

    public User update(UUID id, User user) {
        var existing = findById(id);
        existing.setEmail(user.getEmail());
        existing.setPasswordHash(user.getPasswordHash());
        existing.setRole(user.getRole());
        existing.setStatus(user.getStatus());
        return userRepo.save(existing);
    }

    public void delete(UUID id) {
        userRepo.deleteById(id);
    }
}

