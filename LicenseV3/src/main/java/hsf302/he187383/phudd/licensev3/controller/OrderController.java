package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import hsf302.he187383.phudd.licensev3.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final UserRepository userRepo;
    private final PlanService planService;
    private final OrderService orderService;
    private final WalletService walletService;
    private final LicenseService licenseService;

    @PostMapping("/buy")
    public String buy(@RequestParam UUID planId,
                      @RequestParam(required = false) UUID productId,
                      RedirectAttributes ra) {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        var user = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var plan = planService.findById(planId);

        // 1) Tạo Order PENDING
        var order = orderService.create(user.getId(), plan.getId(), plan.getPriceCredits(), null);

        if (order == null) {
            ra.addFlashAttribute("error", "Mỗi tài khoản chỉ mua 1 lần.");
            return ("redirect:/products/" + productId) ;
        } else {
            try {
                // 2) WalletTxn PURCHASE (OUT) với idempotencyKey
                var idemKey = "ORDER-" + order.getId(); // đủ ổn cho demo
                var txn = walletService.purchase(user.getId(), plan.getPriceCredits(), idemKey, "ORDER", order.getId());

                // 3) Mark PAID + gán txn vào Order
                orderService.markPaidWithTxn(order.getId(), txn.getId());

                // 4) Issue License
                var licenseKey = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                licenseService.issueLicense(order.getId(), licenseKey);

                ra.addFlashAttribute("success", "Mua gói thành công! License key: " + licenseKey);
            } catch (IllegalStateException e) {
                ra.addFlashAttribute("error", "Số dư không đủ. Vui lòng nạp thêm credits trước khi mua.");
            }

            return (productId != null) ? ("redirect:/products/" + productId) : "redirect:/products";
        }
    }
}
