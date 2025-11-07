package hsf302.he187383.phudd.licensev3.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(Exception ex, RedirectAttributes ra) {
        ra.addFlashAttribute("error", "ID không hợp lệ.");
        return "redirect:/";
    }
}

