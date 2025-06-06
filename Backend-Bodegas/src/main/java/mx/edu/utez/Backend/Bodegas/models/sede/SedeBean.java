package mx.edu.utez.Backend.Bodegas.models.sede;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;

import java.util.List;

@Entity
@Data
@Table(name = "sedes")
public class SedeBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, length = 36)
    private String uuid;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String administrador;

    @Column(nullable = false)
    private Boolean status = true;

    @OneToMany(mappedBy = "sede",cascade = CascadeType.ALL) // Aquí Hibernate buscará el campo "sede" en BodegaBean
    @JsonIgnoreProperties("sede")
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public List<BodegaBean> getBodegas() {
        return bodegas;
    }

    public void setBodegas(List<BodegaBean> bodegas) {
        this.bodegas = bodegas;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
