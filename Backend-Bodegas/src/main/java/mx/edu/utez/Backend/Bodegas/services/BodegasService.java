package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.BodegasRepository;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class BodegasService {
    @Autowired
    private BodegasRepository bodegas_Repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //REGEX patterns
    private static final Pattern TIPO_PATTERN = Pattern.compile("^(?! )[A-ZÁÉÍÓÚÑa-záéíóúñ]+(?: [A-ZÁÉÍÓÚÑa-záéíóúñ]+){0,49}$");
    private static final Pattern FOLIO_PATTERN = Pattern.compile("^[A-Z0-9]{5,10}$");
    private static final Pattern PRECIO_PATTERN = Pattern.compile("^(?!\\s*$)\\d+(\\.\\d{1,2})?$");
    private static final Pattern TAMANO_PATTERN = Pattern.compile("^(?! )[A-ZÁÉÍÓÚÑa-záéíóúñ]+(?: [A-ZÁÉÍÓÚÑa-záéíóúñ]+){0,49}$");
    private static final Pattern EDIFICIO_PATTERN = Pattern.compile("^(?! )[A-ZÁÉÍÓÚÑa-záéíóúñ]+(?: [A-ZÁÉÍÓÚÑa-záéíóúñ]+){0,49}$");
    private static final Pattern STATUS_PATTERN=Pattern.compile("DISPONIBLE|RENTADA|POR VENCER|VENCIDA", Pattern.CASE_INSENSITIVE);


    public List<BodegaBean> ObtenerTodas(){
        return bodegas_Repository.findAll();
    }

    public Optional<BodegaBean> BuscarID(Long id){
        return bodegas_Repository.findById(id);
    }

    public BodegaBean CrearBodega(BodegaBean bodega, Long idCliente) {
        validarBodega(bodega);

        Optional<UsuarioBean> cliente = usuarioRepository.findById(idCliente);
        if (!cliente.isPresent()) {
            throw new IllegalArgumentException("Cliente no encontrado");}
        bodega.setCliente(cliente.get());
        bodega.setUuid(UUID.randomUUID().toString());
        return bodegas_Repository.save(bodega);
    }

    public Optional<BodegaBean> ActualizarBodega(Long id, BodegaBean nuevabodega) {
        return bodegas_Repository.findById(id)
                .map(bodegaExistente -> {
                    bodegaExistente.setTipo(nuevabodega.getTipo());
                    bodegaExistente.setPrecio(nuevabodega.getPrecio());
                    bodegaExistente.setFolio(nuevabodega.getFolio());
                    bodegaExistente.setStatus(nuevabodega.getStatus());
                    bodegaExistente.setTamano(nuevabodega.getTamano());
                    bodegaExistente.setEdificio(nuevabodega.getEdificio());
                    return bodegas_Repository.save(bodegaExistente);
                });
    }

    public void EliminarBodega(Long id){
        bodegas_Repository.deleteById(id);
    }

    public Optional<BodegaBean> BuscarPorUUID(String uuid){
        return bodegas_Repository.findByUuid(uuid);
    }

    public List<BodegaBean> obtenerPorCliente(Long idCliente) {
        return bodegas_Repository.findByCliente_Id(idCliente);
    }

    public void validarBodega(BodegaBean bodega)    {
        if(!TIPO_PATTERN.matcher(bodega.getTipo()).matches()){
            throw new IllegalArgumentException("El tipo de bodega no es válido");
        }
        if(!FOLIO_PATTERN.matcher(bodega.getFolio()).matches()){
            throw new IllegalArgumentException("El folio de bodega no es válido");
        }
        if(!PRECIO_PATTERN.matcher(String.valueOf(bodega.getPrecio())).matches()){
            throw new IllegalArgumentException("El precio de bodega no es válido");
        }
        if(!TAMANO_PATTERN.matcher(bodega.getTamano()).matches()){
            throw new IllegalArgumentException("El tamaño de bodega no es válido");
        }
        if(!EDIFICIO_PATTERN.matcher(bodega.getEdificio()).matches()){
            throw new IllegalArgumentException("El edificio de bodega no es válido");
        }
        if(!STATUS_PATTERN.matcher(bodega.getStatus()).matches()){
            throw new IllegalArgumentException("El status de bodega no es válido");
        }
    }
}
