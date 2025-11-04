package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.dto.product.*;
import hsf302.he187383.phudd.license.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ProductResp create(@Valid @RequestBody ProductCreateReq req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public ProductResp get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public ProductResp update(@PathVariable UUID id,
                              @Valid @RequestBody ProductUpdateReq req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping
    public Page<ProductResp> list(
            @RequestParam(value = "q", required = false) String q,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.list(q, pageable);
    }
}