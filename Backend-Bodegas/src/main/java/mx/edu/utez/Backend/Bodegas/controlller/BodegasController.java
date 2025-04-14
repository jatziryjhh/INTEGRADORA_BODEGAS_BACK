package mx.edu.utez.Backend.Bodegas.controlller;

import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import mx.edu.utez.Backend.Bodegas.services.BodegasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/bodegas/")
public class BodegasController {
    @Autowired
    private BodegasService bodegas_services;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<BodegaBean> ObtenerBodegas(){
        return bodegas_services.ObtenerTodas();
    }

    @GetMapping("ID/{id}")
    public ResponseEntity<BodegaBean> BuscarId(@PathVariable Long id){
        Optional<BodegaBean> bodega = bodegas_services.BuscarID(id);
        return ResponseEntity.ok(bodega.get());
    }

    @GetMapping("UUID/{id}")
    public ResponseEntity<BodegaBean> BuscarUiid(@PathVariable String uuid){
        Optional<BodegaBean> bodega = bodegas_services.BuscarPorUUID(uuid);
        return ResponseEntity.ok(bodega.get());
    }

    @PostMapping("crear/")
    public ResponseEntity<BodegaBean> crearBodega(@RequestBody BodegaBean bodega) {
        int cliente_id = bodega.getCliente().getId();
        if (cliente_id != 0) {
            Optional<UsuarioBean> cliente = usuarioRepository.findById((long) cliente_id);
            if (cliente.isPresent()) {
                BodegaBean nuevaBodega = bodegas_services.CrearBodega(bodega, cliente.get().getId());
                return ResponseEntity.ok(nuevaBodega);
            } else {
                return ResponseEntity.badRequest().body(null); // Si no existe el cliente
            }
        }
        return ResponseEntity.badRequest().body(null); // Si no viene el cliente_id o es null
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaBean> actualizarBodega(@PathVariable Long id, @RequestBody BodegaBean nuevaBodega) {
        return bodegas_services.ActualizarBodega(id, nuevaBodega)
                .map(bodega -> ResponseEntity.ok(bodega))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("Eliminar/{id}")
    public ResponseEntity<Void> eliminarBodega(@PathVariable Long id) {
        Optional<BodegaBean> empresa = bodegas_services.BuscarID(id);
        if (empresa.isPresent()) {
            bodegas_services.EliminarBodega(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<BodegaBean>> obtenerBodegasPorCliente(@PathVariable Long id) {
        List<BodegaBean> bodegas = bodegas_services.obtenerPorCliente(id);
        return ResponseEntity.ok(bodegas);
    }
}
