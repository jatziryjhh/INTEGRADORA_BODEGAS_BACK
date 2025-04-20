package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.password.PasswordResetToken;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.PasswordResetTokenRepository;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class RecuperacionService {
    private static final Logger logger = LogManager.getLogger(RecuperacionService.class);
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final int EXPIRACION_MINUTOS = 15;
    @Transactional
    public void enviarCorreoRecuperacion(String email) {
        Optional<UsuarioBean> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            logger.warn("Intento de recuperación fallido: correo no encontrado - {}", email);
            return; // Podrías lanzar una excepción o simplemente no hacer nada por seguridad
        }

        UsuarioBean usuario = usuarioOpt.get();

        // Borrar tokens anteriores si existen
        tokenRepository.deleteByUsuarioId(usuario.getId());
        tokenRepository.flush();
        logger.info("Token anterior eliminado para usuario {}", usuario.getId());

        // Generar token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRACION_MINUTOS);

        // Guardar token
        PasswordResetToken resetToken = new PasswordResetToken(token, expiration, usuario);
        tokenRepository.save(resetToken);

        // Crear link
        String link = frontendUrl + "/reset-password/" + token;

        // Enviar correo
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña:\n\n" + link +
                "\n\nEste enlace expirará en " + EXPIRACION_MINUTOS + " minutos.");

        mailSender.send(message);
        logger.info("Correo de recuperación enviado a {}", email);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) return false;

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            return false;
        }

        UsuarioBean usuario = resetToken.getUsuario();
        usuario.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);
        return true;
    }
}
