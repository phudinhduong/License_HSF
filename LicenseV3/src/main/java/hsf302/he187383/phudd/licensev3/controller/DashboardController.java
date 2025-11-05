package hsf302.he187383.phudd.licensev3.controller;


import hsf302.he187383.phudd.licensev3.model.User;
import hsf302.he187383.phudd.licensev3.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepo;
    private final LicenseRepository licenseRepo;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Authentication auth, Model model) {
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        User user = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("licenses", licenseRepo.findByUserIdOrderByCreatedAtDesc(user.getId()));
        return "dashboard/index";
    }
}
