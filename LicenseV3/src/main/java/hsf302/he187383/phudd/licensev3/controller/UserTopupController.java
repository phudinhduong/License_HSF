package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.config.SecurityConfig;
import hsf302.he187383.phudd.licensev3.enums.TopupStatus;
import hsf302.he187383.phudd.licensev3.model.User;
import hsf302.he187383.phudd.licensev3.repository.UserRepository;
import hsf302.he187383.phudd.licensev3.service.TopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/user/topups")
@RequiredArgsConstructor
public class UserTopupController {
    private final TopupService topupService;
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;

    @GetMapping
    public String listTopups(
            @RequestParam(required = false) TopupStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            Model model
    ) {
        UUID userId = securityConfig.getCurrentUserId();
        User user = userRepository.findById(userId).orElse(null);
        Page<?> topups = topupService.getUserTopups(user, status, from, to, page, size);
        model.addAttribute("topups", topups);
        model.addAttribute("statusOptions", TopupStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "topup/topup-history";
    }
}