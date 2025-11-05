package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.UserStatus;
import hsf302.he187383.phudd.license.model.User;
import hsf302.he187383.phudd.license.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepo;

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

