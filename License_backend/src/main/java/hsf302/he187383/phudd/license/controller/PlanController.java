package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import hsf302.he187383.phudd.license.enums.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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

