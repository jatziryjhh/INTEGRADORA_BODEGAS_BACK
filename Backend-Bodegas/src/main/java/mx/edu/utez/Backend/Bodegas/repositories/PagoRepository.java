package mx.edu.utez.Backend.Bodegas.repositories;

import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.models.pago.PagoBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<PagoBean, Long> {
    Optional<PagoBean> findByUuid(String uuid);
    List<PagoBean> findByFechaVencimientoBetween(LocalDate now, LocalDate fechaLimite);

}
