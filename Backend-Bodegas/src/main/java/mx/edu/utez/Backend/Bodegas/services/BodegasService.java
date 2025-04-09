package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.repositories.BodegasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BodegasService {
    @Autowired
    private BodegasRepository bodegas_Repository;

    public List<BodegaBean> ObtenerTodas(){
        return bodegas_Repository.findAll();
    }

    public Optional<BodegaBean> BuscarID(Long id){
        return bodegas_Repository.findById(id);
    }

    public BodegaBean CrearBodega(BodegaBean bodega){
        bodega.setUuid(UUID.randomUUID().toString());
        return bodegas_Repository.save(bodega);
    }

    public Optional<BodegaBean> ActualizarBodega(Long id, BodegaBean nuevabodega) {
        return bodegas_Repository.findById(id)
                .map(bodegaExistente -> {
                    bodegaExistente.setTipo(nuevabodega.getTipo());
                    bodegaExistente.setPrecio(nuevabodega.getPrecio());
                    bodegaExistente.setStatus(nuevabodega.getStatus());
                    return bodegas_Repository.save(bodegaExistente);
                });
    }

    public void EliminarBodega(Long id){
        bodegas_Repository.deleteById(id);
    }

    public Optional<BodegaBean> BuscarPorUUID(String uuid){
        return bodegas_Repository.findByUuid(uuid);
    }

}
