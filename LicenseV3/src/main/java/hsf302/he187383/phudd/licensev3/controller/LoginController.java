package hsf302.he187383.phudd.licensev3.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

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