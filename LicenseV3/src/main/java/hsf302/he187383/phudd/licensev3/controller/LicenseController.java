package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.enums.AccountStatus;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import hsf302.he187383.phudd.licensev3.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/licenses")
public class LicenseController {

    private final UserRepository userRepo;
    private final LicenseRepository licenseRepo;
    private final AccountRepository accountRepo;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    // ===== Helper: lấy user đang đăng nhập
    private User requireUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("Unauthenticated");
        }
        return userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @GetMapping("/{licenseId}/accounts")
    public String accounts(@PathVariable UUID licenseId, Authentication auth, Model model) {
        var user = requireUser(auth);
        var license = licenseRepo.findByIdAndUserId(licenseId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("License not found"));

        

        model.addAttribute("license", license);
        model.addAttribute("accounts", accountService.findByLicense(licenseId));
        return "licenses/accounts";
    }

    // ===== Form tạo mới
    @GetMapping("/{licenseId}/accounts/new")
    public String newAccount(@PathVariable UUID licenseId, Authentication auth, Model model) {
        var user = requireUser(auth);
        var license = licenseRepo.findByIdAndUserId(licenseId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("License not found"));
        model.addAttribute("license", license);
        return "licenses/account-new";
    }

    // ===== Submit tạo mới
    @PostMapping("/{licenseId}/accounts")
    public String createAccount(@PathVariable UUID licenseId,
                                @RequestParam String username,
                                @RequestParam String password,
                                Authentication auth,
                                RedirectAttributes ra) {
        var user = requireUser(auth);
        var license = licenseRepo.findByIdAndUserId(licenseId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("License not found"));

        // Check seats
        Integer total = license.getSeatsTotal();
        Integer used  = license.getSeatsUsed() == null ? 0 : license.getSeatsUsed();
        if (total != null && total > 0 && used >= total) {
            ra.addFlashAttribute("error", "Hết số lượng chỗ (seats) cho license này.");
            return "redirect:/licenses/" + licenseId + "/accounts";
        }

        try {
            var hash = passwordEncoder.encode(password);
            accountService.create(licenseId, username, hash);
            license.setSeatsUsed(used + 1);
            licenseRepo.save(license);
            ra.addFlashAttribute("success", "Tạo tài khoản thành công.");
            return "redirect:/licenses/" + licenseId + "/accounts";
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Username đã tồn tại.");
            return "redirect:/licenses/" + licenseId + "/accounts/new";
        }
    }

    // ===== Form sửa (đổi mật khẩu)
    @GetMapping("/{licenseId}/accounts/{accountId}/edit")
    public String editAccount(@PathVariable UUID licenseId,
                              @PathVariable UUID accountId,
                              Authentication auth,
                              Model model) {
        var user = requireUser(auth);
        var license = licenseRepo.findByIdAndUserId(licenseId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("License not found"));

        var account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!account.getLicense().getId().equals(license.getId())) {
            throw new IllegalArgumentException("Account not under this license");
        }

        model.addAttribute("license", license);
        model.addAttribute("account", account);
        return "licenses/account-edit";
    }

    // ===== Submit đổi mật khẩu
    @PostMapping("/{licenseId}/accounts/{accountId}/edit")
    public String updateAccountPassword(@PathVariable UUID licenseId,
                                        @PathVariable UUID accountId,
                                        @RequestParam String password,
                                        Authentication auth,
                                        RedirectAttributes ra) {
        var user = requireUser(auth);
        var license = licenseRepo.findByIdAndUserId(licenseId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("License not found"));

        var account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!account.getLicense().getId().equals(license.getId())) {
            throw new IllegalArgumentException("Account not under this license");
        }

        var hash = passwordEncoder.encode(password);
        accountService.updatePassword(accountId, hash);
        ra.addFlashAttribute("success", "Đổi mật khẩu thành công.");
        return "redirect:/licenses/" + licenseId + "/accounts";
    }
}
