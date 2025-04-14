package mx.edu.utez.Backend.Bodegas.models.bodega;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.Backend.Bodegas.models.pago.PagoBean;
import mx.edu.utez.Backend.Bodegas.models.sede.SedeBean;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;

import java.util.List;

@Entity
@Data
@Table(name = "bodegas")

public class BodegaBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(unique = true, length = 36)
    private String uuid;
    @Column(nullable = false)
    private String tipo;
    @Column(nullable = false)
    private String folio;
    @Column(nullable = false)
    private Double precio;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String tamano;
    @Column (nullable = false)
    private String edificio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sede_id",nullable = false) // FK hacia SedeBean
    @JsonIgnoreProperties("bodegas")
    private SedeBean sede;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id",nullable = false)
    @JsonIgnoreProperties("bodegas")
    private UsuarioBean cliente;

    @OneToMany(mappedBy = "bodega",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("bodega")
    private List<PagoBean> pagos;

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

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public SedeBean getSede() {
        return sede;
    }

    public void setSede(SedeBean sede) {
        this.sede = sede;
    }

    public UsuarioBean getCliente() {
        return cliente;
    }

    public void setCliente(UsuarioBean cliente) {
        this.cliente = cliente;
    }

    public List<PagoBean> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoBean> pagos) {
        this.pagos = pagos;
    }
}
