package mx.edu.utez.Backend.Bodegas.models.bodega;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.Backend.Bodegas.models.sede.SedeBean;

@Entity
@Data
@Table(name = "bodegas")

public class BodegaBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true, length = 36)
    String uuid;
    @Column(nullable = false)
    String tipo;
    @Column(nullable = false)
    String folio;
    @Column(nullable = false)
    Double precio;
    @Column(nullable = false)
    String status;

    @ManyToOne
    @JoinColumn(name = "sede_id") // FK hacia SedeBean
    private SedeBean sede;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SedeBean getSede() {
        return sede;
    }

    public void setSede(SedeBean sede) {
        this.sede = sede;
    }
}
