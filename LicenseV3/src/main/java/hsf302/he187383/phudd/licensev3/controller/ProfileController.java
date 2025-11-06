package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.repository.UserRepository;
import hsf302.he187383.phudd.licensev3.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model) {
        var user = currentUserOrThrow();
        model.addAttribute("user", user);
        return "profile/index";
    }

    // Hộp 1: Cập nhật thông tin (không cho đổi email)
    @PostMapping("/profile")
    public String updateProfile(@RequestParam(required = false) String description,
                                @RequestParam(required = false) String fullName,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String address,
                                RedirectAttributes ra) {
        var user = currentUserOrThrow();

        user.setFullName(trimToNull(limit(fullName, 200)));
        user.setPhone(trimToNull(limit(phone, 20)));
        user.setAddress(trimToNull(limit(address, 255)));
        user.setDescription(trimToNull(limit(description, 255)));

        userRepo.save(user);

        ra.addFlashAttribute("ok", "Cập nhật thông tin thành công.");
        return "redirect:/profile";
    }

    // Hộp 2: Đổi mật khẩu
    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 RedirectAttributes ra) {
        var user = currentUserOrThrow();

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            ra.addFlashAttribute("pwdErr", "Mật khẩu cũ không đúng.");
            return "redirect:/profile";
        }
        if (!Objects.equals(newPassword, confirmNewPassword)) {
            ra.addFlashAttribute("pwdErr", "Xác nhận mật khẩu mới không khớp.");
            return "redirect:/profile";
        }
        if (newPassword.length() < 6) {
            ra.addFlashAttribute("pwdErr", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            return "redirect:/profile";
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        ra.addFlashAttribute("pwdOk", "Đổi mật khẩu thành công.");
        return "redirect:/profile";
    }

    // ===== helpers =====
    private User currentUserOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || (auth instanceof AnonymousAuthenticationToken)) {
            throw new IllegalStateException("Unauthenticated");
        }
        String email = auth.getName(); // username = email
        return userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private static String limit(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
    private static String trimToNull(String s) {
        if (!StringUtils.hasText(s)) return null;
        return s.trim();
    }
}

