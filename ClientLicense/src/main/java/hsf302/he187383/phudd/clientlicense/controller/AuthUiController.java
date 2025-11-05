package hsf302.he187383.phudd.clientlicense.controller;


import hsf302.he187383.phudd.clientlicense.LicenseAuthClient;
import hsf302.he187383.phudd.clientlicense.config.SessionUser;
import hsf302.he187383.phudd.clientlicense.dto.LoginResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthUiController {

    private final LicenseAuthClient authClient;

    @GetMapping({"/", "/login"})
    public String loginPage(@RequestParam(value="logout", required=false) String logout,
                            HttpSession session, Model model) {
        var su = (SessionUser) session.getAttribute("user");

        if (su != null) return "redirect:/me";
        if (StringUtils.hasText(logout)) model.addAttribute("msg", "Bạn đã đăng xuất.");
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam(required = false) String deviceId,
                          @RequestParam(required = false, defaultValue = "false") boolean preempt,
                          HttpSession session,
                          Model model) {
        try {
            LoginResponse r = authClient.login(username, password, deviceId, preempt);
            if (r == null || !r.isOk()) {
                model.addAttribute("error", "Đăng nhập thất bại.");
                return "login";
            }
            var lic = r.getLicense();
            var su = SessionUser.builder()
                    .jti(r.getJti())
                    .deviceId(r.getDeviceId())
                    .accountId(r.getAccountId())
                    .username(r.getUsername())
                    .licenseId(lic.getId())
                    .licenseStatus(lic.getStatus())
                    .productCode(lic.getProduct().getCode())
                    .productName(lic.getProduct().getName())
                    .planCode(lic.getPlan().getCode())
                    .planName(lic.getPlan().getName())
                    .billingType(lic.getPlan().getBillingType())
                    .expiresAt(lic.getExpiresAt())
                    .lastHeartbeatAt(java.time.Instant.now())
                    .build();
            session.setAttribute("user", su);
            return "redirect:/me";
        } catch (LicenseAuthClient.ClientException e) {
            model.addAttribute("error", e.getMessage()); // error_description từ A
            return "login";
        }
    }

    @GetMapping("/me")
    public String me(HttpSession session, Model model) {
        var su = (SessionUser) session.getAttribute("user");
        if (su == null) return "redirect:/login";
        model.addAttribute("u", su);
        return "me";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        var su = (SessionUser) session.getAttribute("user");
        if (su != null) {
            try { authClient.logout(su.getJti()); } catch (Exception ignore) {}
            session.invalidate();
        }
        return "redirect:/login?logout=1";
    }
}
