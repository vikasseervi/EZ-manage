package com.vikas.EZmanage.controller;

import com.vikas.EZmanage.dto.AuthResponseDTO;
import com.vikas.EZmanage.dto.LoginRequestDTO;
import com.vikas.EZmanage.dto.SignupRequestDTO;
import com.vikas.EZmanage.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ezmanage/")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO dto) {
        authService.register(dto);
        return ResponseEntity.ok("Signup successful");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        String token = authService.login(dto);
        AuthResponseDTO response = new AuthResponseDTO(token, dto.getUsername(), null); // roles can be added if needed
        return ResponseEntity.ok(response);
    }
}
