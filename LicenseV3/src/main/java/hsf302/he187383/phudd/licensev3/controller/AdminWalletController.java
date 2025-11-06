package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.enums.WalletStatus;
import hsf302.he187383.phudd.licensev3.model.Wallet;
import hsf302.he187383.phudd.licensev3.model.User;
import hsf302.he187383.phudd.licensev3.repository.UserRepository;
import hsf302.he187383.phudd.licensev3.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/wallets")
@RequiredArgsConstructor
public class AdminWalletController {

    private final WalletService walletService;
    private final UserRepository userRepository;

    @GetMapping
    public String listWallets(@RequestParam(required = false) UUID userId,
                              @RequestParam(required = false) WalletStatus status,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "2") int size,
                              Model model) {
        Page<Wallet> wallets = walletService.searchWallets(userId, status, page, size);
        List<User> users = userRepository.findAll();

        model.addAttribute("wallets", wallets);
        model.addAttribute("users", users);
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statusOptions", WalletStatus.values());

        return "wallet/wallet-list";
    }
}
