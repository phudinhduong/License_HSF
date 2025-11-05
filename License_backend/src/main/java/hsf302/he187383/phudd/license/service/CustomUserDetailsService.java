package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.UserStatus;
import hsf302.he187383.phudd.license.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        if (user.getStatus() == UserStatus.DISABLED) {
            throw new UsernameNotFoundException("Disabled");
        }
        return User.withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole().name()) // ADMIN / USER
                .build();
    }
}
