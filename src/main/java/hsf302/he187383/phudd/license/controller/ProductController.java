package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.DTOs.product.*;
import hsf302.he187383.phudd.license.mappers.ProductMapper;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final ProductMapper mapper;

    @PostMapping
    public ProductResponse create(@RequestBody ProductCreateRequest req){
        Product e = mapper.toEntity(req);
        return mapper.toDto(service.create(e));
    }
}
