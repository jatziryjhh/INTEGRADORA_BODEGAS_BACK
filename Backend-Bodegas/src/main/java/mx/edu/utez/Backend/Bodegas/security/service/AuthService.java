package mx.edu.utez.Backend.Bodegas.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import mx.edu.utez.Backend.Bodegas.security.dto.LoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;

    private final String SECRET_KEY = "MI_CLAVE_SECRETA_123"; // Usa un valor más seguro en producción
    private final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public AuthService(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponseDto login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        if (authentication.isAuthenticated()) {
            UsuarioBean usuario = usuarioRepository.findByEmail(email).get();

            String token = JWT.create()
                    .withSubject(email)
                    .withClaim("id", usuario.getId())
                    .withClaim("role", usuario.getRol().name())
                    .withClaim("status", usuario.getStatus())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(SECRET_KEY));

            return new LoginResponseDto(token, usuario.getRol().name(), (int) usuario.getId(), usuario.getStatus());
        }

        throw new RuntimeException("Credenciales inválidas");
    }
}
