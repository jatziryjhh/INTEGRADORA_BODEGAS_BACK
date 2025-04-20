package mx.edu.utez.Backend.Bodegas.models.pago;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.models.sede.SedeBean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "pagos")

public class PagoBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bodega_id", nullable = false)
    @JsonIgnoreProperties("pagos")
    private BodegaBean bodega;

    @Column(nullable = false)
    private Double monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name="fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "recordatorio_enviado", nullable = true)
    private Boolean recordatorioEnviado=false;

    public Boolean getRecordatorioEnviado() {
        return recordatorioEnviado;
    }

    public void setRecordatorioEnviado(Boolean recordatorioEnviado) {
        this.recordatorioEnviado = recordatorioEnviado;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BodegaBean getBodega() {
        return bodega;
    }

    public void setBodega(BodegaBean bodega) {
        this.bodega = bodega;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @PrePersist
    public void setFechaVencimiento() {
        if (this.fechaPago == null) {
            this.fechaPago = LocalDate.now();  // Asigna la fecha actual como fecha de pago
        }
        // Asigna la fecha de vencimiento 1 mes después de la fecha de pago
        //this.fechaVencimiento = this.fechaPago.plusMonths(1);
        this.fechaVencimiento = LocalDateTime.now().plusMinutes(5).toLocalDate();  // 5 minutos después de la fecha de pago
    }
}
