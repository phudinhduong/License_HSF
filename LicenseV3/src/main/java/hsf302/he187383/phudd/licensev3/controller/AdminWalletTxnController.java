package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.enums.Ref_Type;
import hsf302.he187383.phudd.licensev3.enums.WalletTxnDirection;
import hsf302.he187383.phudd.licensev3.model.WalletTxn;
import hsf302.he187383.phudd.licensev3.repository.WalletRepository;
import hsf302.he187383.phudd.licensev3.service.WalletTxnService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/admin/wallet-txns")
@RequiredArgsConstructor
public class AdminWalletTxnController {

    private final WalletTxnService walletTxnService;
    private final WalletRepository walletRepository;

    @GetMapping
    public String listTxns(@RequestParam(required = false) UUID walletId,
                           @RequestParam(required = false) WalletTxnDirection direction,
                           @RequestParam(required = false) Ref_Type refType,
                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model) {

        Page<WalletTxn> txns = walletTxnService.searchTxns(walletId, direction, refType, from, to, page, size);

        model.addAttribute("txns", txns);
        model.addAttribute("wallets", walletRepository.findAll());
        model.addAttribute("selectedWalletId", walletId);
        model.addAttribute("selectedDirection", direction);
        model.addAttribute("selectedRefType", refType);
        model.addAttribute("refTypeOptions", Ref_Type.values());
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "wallet/walletTxn-list";
    }
}
