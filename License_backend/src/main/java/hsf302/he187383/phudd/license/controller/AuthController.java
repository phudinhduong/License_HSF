package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.dto.auth.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.service.*;
import io.jsonwebtoken.*;
import jakarta.validation.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResp login(@Valid @RequestBody LoginReq req) {
        return authService.login(req);
    }

    @PostMapping("/refresh")
    public TokenResp refresh(@Valid @RequestBody RefreshReq req) {
        return authService.refresh(req);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public TokenResp register(@Valid @RequestBody SignupReq req) {
        return authService.register(req);
    }
}