package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.pago.PagoBean;
import mx.edu.utez.Backend.Bodegas.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class PagosService {
    @Autowired
    private PagoRepository pagoRepository;

    public List<PagoBean> obtenerTodosLosPagos() {
        return pagoRepository.findAll();
    }

    public Optional<PagoBean> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public PagoBean crearPago(PagoBean pago) {
        pago.setUuid(UUID.randomUUID().toString());
        pago.setFechaPago(LocalDate.now());
        return pagoRepository.save(pago);
    }

    public Optional<PagoBean> actualizarPago(Long id, PagoBean nuevoPago) {
        return pagoRepository.findById(id)
                .map(pagoExistente -> {
                    pagoExistente.setMonto(nuevoPago.getMonto());
                    pagoExistente.setFechaPago(nuevoPago.getFechaPago());
                    pagoExistente.setBodega(nuevoPago.getBodega());
                    return pagoRepository.save(pagoExistente);
                });
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }

    public Optional<PagoBean> buscarPorUUID(String uuid) {
        return pagoRepository.findByUuid(uuid);
    }

}
