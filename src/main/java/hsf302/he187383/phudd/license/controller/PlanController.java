package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.DTOs.product.*;
import hsf302.he187383.phudd.license.mappers.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService service;
    private final PlanMapper mapper;


    @PostMapping
    public PlanResponse create(@RequestBody PlanCreateRequest req){
        Plan e = mapper.toEntity(req);
        return mapper.toDto(service.create(e));
    }
}
