package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.dto.plan.*;
import hsf302.he187383.phudd.license.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService service;

    //tested api
    @PostMapping
    public PlanResp create(@Valid @RequestBody PlanCreateReq req) {
        return service.create(req);
    }

    //tested api
    @PutMapping("/{id}")
    public PlanResp update(@PathVariable UUID id,
                           @Valid @RequestBody PlanUpdateReq req) {
        // đảm bảo path id khớp với req.id (nếu bé muốn)
        if (req.getId() != null && !req.getId().equals(id)) {
            throw new IllegalArgumentException("Mismatched id");
        }
        return service.update(id, req);
    }

    //tested api
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    //tested api
    @GetMapping("/{id}")
    public PlanResp get(@PathVariable UUID id) {
        return service.get(id);
    }

    //tested api
    @GetMapping
    public Page<PlanResp> list(
            @RequestParam(value = "productId", required = false) UUID productId,
            @RequestParam(value = "q", required = false) String q,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.list(productId, q, pageable);
    }
}
