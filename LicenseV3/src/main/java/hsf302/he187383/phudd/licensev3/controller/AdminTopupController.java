package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.enums.TopupStatus;
import hsf302.he187383.phudd.licensev3.model.Topup;
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
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/topups")
@RequiredArgsConstructor
public class AdminTopupController {

    private final TopupService topupService;
    private final UserRepository userRepository;

    @GetMapping
    public String listTopups(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        TopupStatus statusEnum = null;
        if (status != null && !status.isBlank() && !"All".equalsIgnoreCase(status)) {
            try {
                statusEnum = TopupStatus.valueOf(status);
            } catch (IllegalArgumentException ignored) {}
        }

        Page<Topup> topups = topupService.getAllTopupsForAdmin(userId, statusEnum, from, to, page, size);
        List<User> users = userRepository.findAll();

        model.addAttribute("topups", topups);
        model.addAttribute("users", users);
        model.addAttribute("statusOptions", TopupStatus.values());
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "topup/topup-list";
    }
}
