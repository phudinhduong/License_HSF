package hsf302.he187383.phudd.licensev3.controller;


import hsf302.he187383.phudd.licensev3.model.Plan;
import hsf302.he187383.phudd.licensev3.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PlanService planService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        var product = productService.findById(id); // ném IllegalArgumentException nếu không thấy
        model.addAttribute("product", product);

        List<Plan> plans = planService.findByProduct(id);
        for (Plan plan : plans) {
            System.out.println(plan.toString());
        }

        model.addAttribute("plans", planService.findByProduct(id));
        return "products/detail";
    }

    // (Optional) handler 404 gọn khi service ném IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound() {
        return "errors/404"; // tạo file nếu muốn trang 404 đẹp
    }
}
