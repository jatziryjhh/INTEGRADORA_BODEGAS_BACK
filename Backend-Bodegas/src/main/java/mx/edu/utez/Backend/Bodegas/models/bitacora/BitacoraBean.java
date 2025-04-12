package mx.edu.utez.Backend.Bodegas.models.bitacora;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
public class BitacoraBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    private String rol;

    private String nombre;

    private String metodo;

    private String ruta;

    private LocalDateTime fechaHora;

    public BitacoraBean() {
    }

    public BitacoraBean(Long id, Long usuarioId, String rol, String nombre, String metodo, String ruta, LocalDateTime fechaHora) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.rol = rol;
        this.nombre = nombre;
        this.metodo = metodo;
        this.ruta = ruta;
        this.fechaHora = fechaHora;
    }

    // Builder manual
    public static class Builder {
        private Long usuarioId;
        private String rol;
        private String nombre;
        private String metodo;
        private String ruta;
        private LocalDateTime fechaHora;

        public Builder usuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public Builder rol(String rol) {
            this.rol = rol;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder metodo(String metodo) {
            this.metodo = metodo;
            return this;
        }

        public Builder ruta(String ruta) {
            this.ruta = ruta;
            return this;
        }

        public Builder fechaHora(LocalDateTime fechaHora) {
            this.fechaHora = fechaHora;
            return this;
        }

        public BitacoraBean build() {
            return new BitacoraBean(null, usuarioId, rol, nombre, metodo, ruta, fechaHora);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
