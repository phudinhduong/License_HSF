package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.DTOs.org.*;
import hsf302.he187383.phudd.license.mappers.*;
import hsf302.he187383.phudd.license.model.User;
import hsf302.he187383.phudd.license.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    //tạo
    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest req){
        User e = service.create(mapper.toEntity(req));
        return mapper.toDto(e);
    }

}
