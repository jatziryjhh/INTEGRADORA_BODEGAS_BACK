package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuariosService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioBean> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioBean> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public UsuarioBean crearUsuario(UsuarioBean usuario) {
        usuario.setUuid(UUID.randomUUID().toString());
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioBean> actualizarUsuario(Long id, UsuarioBean nuevoUsuario) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setEmail(nuevoUsuario.getEmail());
                    usuarioExistente.setPassword(nuevoUsuario.getPassword());
                    usuarioExistente.setRol(nuevoUsuario.getRol());
                    return usuarioRepository.save(usuarioExistente);
                });
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<UsuarioBean> buscarPorUUID(String uuid) {
        return usuarioRepository.findByUuid(uuid);
    }
}
