package mx.edu.utez.Backend.Bodegas.repositories;

import mx.edu.utez.Backend.Bodegas.models.password.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUsuarioId(int usuarioId);
}