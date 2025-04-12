package mx.edu.utez.Backend.Bodegas.controlller;

import mx.edu.utez.Backend.Bodegas.models.bitacora.BitacoraBean;
import mx.edu.utez.Backend.Bodegas.services.BitacoraService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/bitacora")
public class BitacoraController {
    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }


    @GetMapping("/")
    public List<BitacoraBean> obtenerBitacora() {
        return bitacoraService.ObtenerBitacora();
    }
}
