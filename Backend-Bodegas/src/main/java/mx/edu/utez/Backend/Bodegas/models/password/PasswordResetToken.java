package mx.edu.utez.Backend.Bodegas.models.password;

import jakarta.persistence.*;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;

import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioBean usuario;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, LocalDateTime expirationDate, UsuarioBean usuario) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UsuarioBean getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }
}