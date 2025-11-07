package hsf302.he187383.phudd.licensev3.controller;


import hsf302.he187383.phudd.licensev3.model.User;
import hsf302.he187383.phudd.licensev3.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepo;
    private final LicenseRepository licenseRepo;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Authentication auth,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        var user = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Ràng buộc size hợp lý (1..50)
        size = Math.max(1, Math.min(size, 50));
        page = Math.max(0, page);

        var pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt")
        );

        var p = licenseRepo.findByUserId(user.getId(), pageable);

        model.addAttribute("licenses", p.getContent());
        model.addAttribute("page", p);
        model.addAttribute("size", p.getSize());
        model.addAttribute("currentPage", p.getNumber());
        model.addAttribute("totalPages", p.getTotalPages());
        model.addAttribute("totalElements", p.getTotalElements());

        // Hiển thị "từ–đến"
        int start = p.getNumber() * p.getSize() + (p.getTotalElements() == 0 ? 0 : 1);
        int end   = start + p.getNumberOfElements() - (p.getNumberOfElements() == 0 ? 0 : 1);
        model.addAttribute("rangeStart", start);
        model.addAttribute("rangeEnd", end);

        return "dashboard/index";
    }

}
