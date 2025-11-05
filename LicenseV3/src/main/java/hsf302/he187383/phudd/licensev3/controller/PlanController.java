package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanRepository planRepo;
    private final ProductRepository productRepo;

    @GetMapping
    public String list(@RequestParam(value = "productId", required = false) UUID productId,
                       Model model) {
        List<Plan> plans;
        if (productId != null) {
            plans = planRepo.findByProductIdOrderByCreatedAtDesc(productId);
            model.addAttribute("selectedProduct", productRepo.findById(productId).orElse(null));
        } else {
            plans = planRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        model.addAttribute("plans", plans);
        model.addAttribute("products", productRepo.findAll());
        return "plans/list"; // /templates/plans/list.html
    }
}

