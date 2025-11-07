package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.model.Plan;
import hsf302.he187383.phudd.licensev3.service.PlanService;
import hsf302.he187383.phudd.licensev3.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin/plan")
@RequiredArgsConstructor
public class AdminPlanController {

    private final PlanService planService;
    private final ProductService productService;

    @GetMapping
    public String getListPlans(Model model) {
        System.out.println("lấy all plan");
        model.addAttribute("plans", planService.findAll());
        model.addAttribute("plan", new Plan());
        model.addAttribute("products", productService.findAll());
        return "admin/list-plan";
    }

    @GetMapping("/update/{id}")
    public String bindPlanToForm(@PathVariable UUID id, Model model) {
        model.addAttribute("plan", planService.findById(id));
        model.addAttribute("plans", planService.findAll());
        return "admin/list-plan";
    }

    @PostMapping()
    public String savePlan(@ModelAttribute Plan plan, @RequestParam(value = "id", required = false ) UUID id) {
        if(id == null) {
            planService.create(plan);
        } else {
            try {
                planService.update(id, plan);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return "redirect:/admin/plan";
    }

    @GetMapping("/delete/{id}")
    public String deletePlan(@PathVariable UUID id) {
        planService.delete(id);
        return "redirect:/admin/plan";
    }
}
