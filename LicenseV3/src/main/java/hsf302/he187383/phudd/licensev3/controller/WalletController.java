package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.model.Topup;
import hsf302.he187383.phudd.licensev3.repository.UserRepository;
import hsf302.he187383.phudd.licensev3.service.TopupService;
import hsf302.he187383.phudd.licensev3.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final TopupService topupService;
    private final VNPayService vnPayService;
    private final UserRepository userRepository;

    @GetMapping("/topup")
    public String showTopupPage() {
        return "wallet/topup"; // -> resources/templates/wallet/topup.html
    }


    @PostMapping("/topup")
    public String createTopup(HttpServletRequest request,
                              @RequestParam("amount") BigDecimal amount,
                              @RequestParam("orderInfo") String orderInfo) {

        UUID userId = getCurrentUserId();

        Topup topup = topupService.createPendingTopup(userId, amount);

        String paymentUrl = vnPayService.createPaymentUrl(
                request,
                amount.longValue(),
                orderInfo,
                topup.getPaymentRef()
        );

        return "redirect:" + paymentUrl;
    }


    @GetMapping("/vnpay-return")
    public String handleVNPayReturn(HttpServletRequest request, Model model) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> params.put(key, values[0]));

        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        BigDecimal amount = new BigDecimal(params.get("vnp_Amount")).divide(BigDecimal.valueOf(100));

        try {
            boolean isValid = vnPayService.validatePaymentResponse(new HashMap<>(params));

            if (isValid && "00".equals(responseCode)) {
                topupService.markAsSuccess(txnRef, params.get("vnp_TransactionNo"));
                model.addAttribute("status", "success");
                model.addAttribute("message", "Thanh toán thành công!");
            } else {
                topupService.markAsFailed(txnRef);
                model.addAttribute("status", "failed");
                model.addAttribute("message", "Thanh toán thất bại hoặc bị hủy!");
            }
        } catch (Exception e) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Đã xảy ra lỗi trong quá trình xác minh thanh toán.");
            e.printStackTrace();
        }

        model.addAttribute("txnRef", txnRef);
        model.addAttribute("amount", amount); // đã chia 100
        model.addAttribute("transactionNo", params.get("vnp_TransactionNo"));
        model.addAttribute("payDate", params.get("vnp_PayDate"));
        model.addAttribute("orderInfo", params.get("vnp_OrderInfo"));

        return "wallet/topup-result";
    }


    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername(); // email = username
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            return user.getId();
        }

        throw new IllegalStateException("No authenticated user found");
    }
}
