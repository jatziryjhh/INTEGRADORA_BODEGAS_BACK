package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.sede.SedeBean;
import mx.edu.utez.Backend.Bodegas.repositories.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SedesService {
    @Autowired
    private SedeRepository sedeRepository;

    public List<SedeBean> obtenerTodasLasSedes() {
        return sedeRepository.findAll();
    }

    public Optional<SedeBean> buscarPorId(Long id) {
        return sedeRepository.findById(id);
    }

    public SedeBean crearSede(SedeBean sede) {
        sede.setUuid(UUID.randomUUID().toString());
        return sedeRepository.save(sede);
    }

    public Optional<SedeBean> actualizarSede(Long id, SedeBean nuevaSede) {
        return sedeRepository.findById(id)
                .map(sedeExistente -> {
                    sedeExistente.setNombre(nuevaSede.getNombre());
                    sedeExistente.setBodegas(nuevaSede.getBodegas());
                    return sedeRepository.save(sedeExistente);
                });
    }

    public void eliminarSede(Long id) {
        sedeRepository.deleteById(id);
    }

    public Optional<SedeBean> buscarPorUUID(String uuid) {
        return sedeRepository.findByUuid(uuid);
    }
}
