package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.bitacora.BitacoraBean;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.BitacoraRepository;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitacoraService {
    @Autowired
    private BitacoraRepository bitacoraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void guardarActividad(Long usuarioId, String rol, String accion, String endpoint) {
        UsuarioBean usuario = usuarioRepository.findById(usuarioId).orElse(null);
        String nombre = (usuario != null) ? usuario.getNombre() : "Desconocido";

        //BitacoraBean actividad = new BitacoraBean(usuarioId, rol, nombre, accion, endpoint);
        //bitacoraRepository.save(actividad);
    }
}
