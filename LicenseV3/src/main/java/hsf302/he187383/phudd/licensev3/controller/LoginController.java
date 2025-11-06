package hsf302.he187383.phudd.licensev3.controller;


import hsf302.he187383.phudd.licensev3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Nếu user đã đăng nhập thì không cần quay lại login nữa
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }

        return "auth/login"; // → /templates/auth/login.html
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "auth/logout-success";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        // giữ lại giá trị cũ nếu có
        if (!model.containsAttribute("email")) model.addAttribute("email", "");
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             Model model) {

        email = email == null ? "" : email.trim();
        var errors = new ArrayList<String>();

        // Validate tay (không dùng @Valid)
        if (email.isEmpty()) {
            errors.add("Email không được để trống.");
        } else if (!isEmailLike(email)) {
            errors.add("Email không hợp lệ.");
        }
        if (password == null || password.length() < 6) {
            errors.add("Mật khẩu phải có ít nhất 6 ký tự.");
        }
        if (!Objects.equals(password, confirmPassword)) {
            errors.add("Xác nhận mật khẩu không khớp.");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("email", email);
            return "auth/register";
        }

        try {
            userService.registerNewUser(email, password);
        } catch (IllegalArgumentException ex) {
            if ("email_exists".equals(ex.getMessage())) {
                model.addAttribute("errors", List.of("Email đã được sử dụng."));
                model.addAttribute("email", email);
                return "auth/register";
            }
            model.addAttribute("errors", List.of("Không thể đăng ký. Vui lòng thử lại."));
            model.addAttribute("email", email);
            return "auth/register";
        }

        return "redirect:/login?registered";
    }

    // check email đơn giản (đủ dùng cho UI, backend vẫn dựa unique index)
    private boolean isEmailLike(String s) {
        // rất đơn giản: có @ và có dấu chấm sau @
        int at = s.indexOf('@');
        int dot = s.lastIndexOf('.');
        return at > 0 && dot > at + 1 && dot < s.length() - 1;
    }

//    @GetMapping("/")
//    public String home() {
//        return "redirect:/dashboard";
//    }
//
//
//    @GetMapping("/dashboard")
//    public String dashboard(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
//            model.addAttribute("username", auth.getName());
//        }
//        return "dashboard/index"; // /templates/dashboard/index.html
//    }
}