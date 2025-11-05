package hsf302.he187383.phudd.licensev3.config;

import hsf302.he187383.phudd.licensev3.model.User;
import hsf302.he187383.phudd.licensev3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final UserRepository userRepo;

    // DTO gọn cho view
    public record CurrentUserVM(UUID id, String email, String role, String displayName) {}

    @ModelAttribute("currentUser")
    public CurrentUserVM addCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        String email = auth.getName(); // SecurityConfig map username = email
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return new CurrentUserVM(null, email, null, email);
        return new CurrentUserVM(user.getId(), user.getEmail(), user.getRole().name(), user.getEmail());
    }
}
