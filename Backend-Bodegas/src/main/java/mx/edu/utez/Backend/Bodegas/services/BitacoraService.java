package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.bitacora.BitacoraBean;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.BitacoraRepository;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BitacoraService {
    @Autowired
    private BitacoraRepository bitacoraRepository;

    public List<BitacoraBean> ObtenerBitacora(){
        return bitacoraRepository.findAll();
    }
}
