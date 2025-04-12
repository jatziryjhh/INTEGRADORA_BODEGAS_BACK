package mx.edu.utez.Backend.Bodegas.models.bitacora;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.Backend.Bodegas.repositories.BitacoraRepository;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import mx.edu.utez.Backend.Bodegas.security.jwt.JwtAuthFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
@Component
public class BitacoraInterceptor implements HandlerInterceptor {
    private static final Logger logger = LogManager.getLogger(BitacoraInterceptor.class);

    private final JwtAuthFilter jwtUtils;
    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;

    public BitacoraInterceptor(JwtAuthFilter jwtUtils, BitacoraRepository bitacoraRepository, UsuarioRepository usuarioRepository) {
        this.jwtUtils = jwtUtils;
        this.bitacoraRepository = bitacoraRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.replace("Bearer ", "");
                DecodedJWT decodedJWT = jwtUtils.decodeToken(token);

                Long userId = decodedJWT.getClaim("id").asLong();
                String rol = decodedJWT.getClaim("role").asString();

                String nombre = usuarioRepository.findById(userId)
                        .map(u -> u.getNombre() + " " + u.getApellidoPaterno())
                        .orElse("Desconocido");

                BitacoraBean bitacora = BitacoraBean.builder()
                        .usuarioId(userId)
                        .rol(rol)
                        .nombre(nombre)
                        .metodo(request.getMethod())
                        .ruta(request.getRequestURI())
                        .fechaHora(LocalDateTime.now())
                        .build();

                bitacoraRepository.save(bitacora);
                logger.info("Bitácora registrada: usuario={} rol={} ruta={} método={}", nombre, rol, request.getRequestURI(), request.getMethod());
            } catch (Exception e) {
                logger.error("Error al registrar en bitácora: {}", e.getMessage(), e);
            }
        } else {
            logger.warn("Solicitud sin token Bearer: {}", request.getRequestURI());
        }
        return true;
    }
}