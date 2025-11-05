package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class VNPayController {

    private final VNPayService vnPayService;

//    // Tạo thanh toán VNPAY: test bằng GET hoặc gọi từ form
//    @GetMapping("/create-payment")
//    public String createPayment(HttpServletRequest request,
//                                @RequestParam("amount") BigDecimal amount,
//                                @RequestParam(value = "orderInfo", defaultValue = "Thanh toán thuê xe") String orderInfo) {
//        long amountInVND = amount.longValue();
//        String paymentUrl = vnPayService.createPaymentUrl(request, amountInVND, orderInfo);
//        return "redirect:" + paymentUrl;
//    }

    // Return URL sau khi khách hàng thanh toán xong
    @GetMapping("/vnpay-return")
    public String handleReturn(HttpServletRequest request, Model model) {
        Map<String, String> params = extractParams(request);
        boolean valid = vnPayService.validatePaymentResponse(new HashMap<>(params));

        String responseCode = params.get("vnp_ResponseCode");
        String transactionStatus = params.get("vnp_TransactionStatus");

        if (valid && "00".equals(responseCode) && "00".equals(transactionStatus)) {
            model.addAttribute("message", "Giao dịch thành công!");
        } else {
            model.addAttribute("message", "Giao dịch thất bại hoặc không hợp lệ!");
        }

        model.addAttribute("params", params);
        return "payment-result"; // tên file .html trong templates/
    }

    // IPN URL: VNPAY gọi trực tiếp về server của bạn
    @PostMapping("/vnpay-ipn")
    @ResponseBody
    public Map<String, String> handleIpn(HttpServletRequest request) {
        Map<String, String> params = extractParams(request);
        boolean valid = vnPayService.validatePaymentResponse(new HashMap<>(params));

        Map<String, String> response = new HashMap<>();
        if (valid && "00".equals(params.get("vnp_ResponseCode"))) {
            // TODO: update trạng thái đơn hàng tại đây (order status = PAID)
            response.put("RspCode", "00");
            response.put("Message", "Confirm Success");
        } else {
            response.put("RspCode", "97");
            response.put("Message", "Invalid Signature");
        }
        return response;
    }

    private Map<String, String> extractParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> fields = request.getParameterNames();
        while (fields.hasMoreElements()) {
            String fieldName = fields.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                params.put(fieldName, fieldValue);
            }
        }
        return params;
    }
}
