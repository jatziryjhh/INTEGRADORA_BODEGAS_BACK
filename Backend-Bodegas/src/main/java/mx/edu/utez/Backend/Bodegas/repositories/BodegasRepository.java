package mx.edu.utez.Backend.Bodegas.repositories;

import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BodegasRepository extends JpaRepository <BodegaBean, Long> {
    Optional<BodegaBean> findByUuid(String uuid);

    List<BodegaBean> findByCliente_Id(Long id);

}
