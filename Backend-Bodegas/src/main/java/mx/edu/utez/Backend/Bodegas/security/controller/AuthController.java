package mx.edu.utez.Backend.Bodegas.security.controller;

import mx.edu.utez.Backend.Bodegas.security.dto.LoginDto;
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
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        try {
            // Usamos un rol mockeado por ahora, puedes obtenerlo de la base de datos o del objeto Usuario
            String token = authService.login(loginDto.getUsername(), loginDto.getPassword(), "USER_ROLE");
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
