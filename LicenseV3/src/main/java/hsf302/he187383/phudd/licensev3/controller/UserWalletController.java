package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.config.SecurityConfig;
import hsf302.he187383.phudd.licensev3.enums.Ref_Type;
import hsf302.he187383.phudd.licensev3.enums.WalletTxnDirection;
import hsf302.he187383.phudd.licensev3.model.Wallet;
import hsf302.he187383.phudd.licensev3.model.WalletTxn;
import hsf302.he187383.phudd.licensev3.service.WalletService;
import hsf302.he187383.phudd.licensev3.service.WalletTxnService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Controller
@RequestMapping("/my-wallet")
@RequiredArgsConstructor
public class UserWalletController {

    private final WalletService walletService;
    private final WalletTxnService walletTxnService;
    private final SecurityConfig securityService;

    @GetMapping
    public String viewWallet(@RequestParam(required = false) WalletTxnDirection direction,
                             @RequestParam(required = false) Ref_Type refType,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {

        // Lấy ID người dùng hiện tại
        UUID userId = securityService.getCurrentUserId();

        // Tìm ví của người dùng
        Wallet wallet = walletService.getWalletByUserId(userId);
        if (wallet == null) {
            model.addAttribute("error", "Wallet not found.");
            return "wallet/user-wallet";
        }

        // Chuyển đổi thời gian lọc
        Instant fromInstant = (from != null) ? from.atZone(ZoneId.systemDefault()).toInstant() : null;
        Instant toInstant = (to != null) ? to.atZone(ZoneId.systemDefault()).toInstant() : null;

        // Truy vấn danh sách giao dịch
        Page<WalletTxn> txns = walletTxnService.searchTxns(wallet.getId(), direction, refType, fromInstant, toInstant, page, size);

        // Truyền dữ liệu sang view
        model.addAttribute("wallet", wallet);
        model.addAttribute("txns", txns);
        model.addAttribute("selectedDirection", direction);
        model.addAttribute("selectedRefType", refType);
        model.addAttribute("refTypeOptions", Ref_Type.values());
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "wallet/user-wallet";
    }
}
