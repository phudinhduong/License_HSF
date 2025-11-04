package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.UserStatus;
import hsf302.he187383.phudd.license.model.User;
import hsf302.he187383.phudd.license.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = repo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (u.getStatus() != UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("User disabled");
        }

        List<GrantedAuthority> auth = List.of(new SimpleGrantedAuthority(u.getRole().name()));

//        System.out.println("auth: " + auth);

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPasswordHash(),
                auth
        );
    }
}
