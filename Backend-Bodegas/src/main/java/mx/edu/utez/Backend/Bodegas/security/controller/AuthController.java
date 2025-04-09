package mx.edu.utez.Backend.Bodegas.security.controller;

import mx.edu.utez.Backend.Bodegas.security.dto.LoginDto;
import mx.edu.utez.Backend.Bodegas.security.dto.LoginResponseDto;
import mx.edu.utez.Backend.Bodegas.security.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        try {
            LoginResponseDto response = authService.login(loginDto.getEmail(), loginDto.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }
}