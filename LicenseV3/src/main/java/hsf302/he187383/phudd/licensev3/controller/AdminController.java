package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.model.Product;
import hsf302.he187383.phudd.licensev3.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;

    // Danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/list-product";
    }

    // Hiển thị form thêm
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/form-product";
    }

    // Xử lý thêm sản phẩm
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.create(product);
        return "redirect:/admin";
    }

    // Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "admin/form-product"; // dùng lại cùng template
    }

    // Xử lý cập nhật sản phẩm
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable UUID id, @ModelAttribute Product product) {
        productService.update(id, product);
        return "redirect:/admin";
    }

    // Ẩn sản phẩm (soft delete)
    @PostMapping("/hide/{id}")
    public String hideProduct(@PathVariable UUID id) {
        Product p = productService.findById(id);
        p.setHidden(true);
        productService.update(p.getId(), p);
        return "redirect:/admin";
    }

    // Hiển thị lại sản phẩm bị ẩn
    @PostMapping("/unhide/{id}")
    public String unhideProduct(@PathVariable UUID id) {
        Product p = productService.findById(id);
        p.setHidden(false);
        productService.update(p.getId(), p);
        return "redirect:/admin";
    }

    // Xóa sản phẩm
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/admin";
    }
}
