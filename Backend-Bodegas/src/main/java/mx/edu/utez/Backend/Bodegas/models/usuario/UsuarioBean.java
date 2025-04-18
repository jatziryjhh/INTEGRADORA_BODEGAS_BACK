package mx.edu.utez.Backend.Bodegas.models.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;

import java.util.List;

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

    @Column(nullable = false)
    private Boolean status = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role rol;

    @OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cliente")
    private List<BodegaBean> bodegas;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigopos() {
        return codigopos;
    }

    public List<BodegaBean> getBodegas() {
        return bodegas;
    }

    public void setBodegas(List<BodegaBean> bodegas) {
        this.bodegas = bodegas;
    }

    public void setCodigopos(String codigopos) {
        this.codigopos = codigopos;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}