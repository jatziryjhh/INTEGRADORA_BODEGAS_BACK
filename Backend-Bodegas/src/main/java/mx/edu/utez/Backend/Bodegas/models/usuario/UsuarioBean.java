package mx.edu.utez.Backend.Bodegas.models.usuario;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "usuarios")
public class UsuarioBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, length = 36)
    private String uuid;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidoMaterno;

    @Column(nullable = false)
    private String apellidoPaterno;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String rfc;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String codigopos;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rol;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }
}
