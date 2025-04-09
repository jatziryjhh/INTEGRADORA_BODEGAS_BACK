package mx.edu.utez.Backend.Bodegas.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Aquí normalmente cargarías el usuario desde la base de datos
        // Por ahora vamos a simular uno para que no marque error
        if ("admin".equals(username)) {
            return new User(
                    "admin",
                    "$2a$10$rX8F7U8wWc2NEZ2iN3.TgO7YUJkzTqvhX0gyE4qHOBBsFoMQ5tKve", // "1234" en bcrypt
                    Collections.singletonList(() -> "ROLE_ADMIN")
            );
        }
        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}
