package hsf302.he187383.phudd.licensev3.controller;


import hsf302.he187383.phudd.licensev3.model.Plan;
import hsf302.he187383.phudd.licensev3.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PlanService planService;

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {

        var p = productService.findPage(q, page, size);

        model.addAttribute("products", p.getContent());
        model.addAttribute("page", p);
        model.addAttribute("size", p.getSize());
        model.addAttribute("currentPage", p.getNumber());
        model.addAttribute("totalPages", p.getTotalPages());
        model.addAttribute("totalElements", p.getTotalElements());
        model.addAttribute("q", q);

        int start = p.getNumber() * p.getSize() + (p.getTotalElements() == 0 ? 0 : 1);
        int end   = start + p.getNumberOfElements() - (p.getNumberOfElements() == 0 ? 0 : 1);
        model.addAttribute("rangeStart", start);
        model.addAttribute("rangeEnd", end);

        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model, RedirectAttributes ra
    ) {

        var product = productService.findById(id); // ném IllegalArgumentException nếu không thấy

        if (product == null) {
            ra.addFlashAttribute("message", "Product not found");
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        var p = planService.findByProductPaged(id, page, size);

        model.addAttribute("plans", p.getContent());
        model.addAttribute("page", p);
        model.addAttribute("size", p.getSize());
        model.addAttribute("currentPage", p.getNumber());
        model.addAttribute("totalPages", p.getTotalPages());
        model.addAttribute("totalElements", p.getTotalElements());

        int start = p.getTotalElements() == 0 ? 0 : (p.getNumber() * p.getSize() + 1);
        int end   = start == 0 ? 0 : (start + p.getNumberOfElements() - 1);
        model.addAttribute("rangeStart", start);
        model.addAttribute("rangeEnd", end);

        return "products/detail";
    }

    // (Optional) handler 404 gọn khi service ném IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound() {
        return "errors/404"; // tạo file nếu muốn trang 404 đẹp
    }
}
