package mx.edu.utez.Backend.Bodegas.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final String SECRET_KEY = "MI_CLAVE_SECRETA_123"; // Usa un valor más seguro en producción
    private final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public String login(String username, String password, String role) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("role", role)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(Algorithm.HMAC256(SECRET_KEY));
        }

        throw new RuntimeException("Credenciales inválidas");
    }
}
