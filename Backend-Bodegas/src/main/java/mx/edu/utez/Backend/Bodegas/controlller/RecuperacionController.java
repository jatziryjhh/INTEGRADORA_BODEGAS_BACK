package mx.edu.utez.Backend.Bodegas.controlller;
import mx.edu.utez.Backend.Bodegas.models.password.EmailDTO;
import mx.edu.utez.Backend.Bodegas.models.password.ResetPasswordDTO;
import mx.edu.utez.Backend.Bodegas.services.RecuperacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RecuperacionController {
    private final RecuperacionService recuperacionService;
    public RecuperacionController(RecuperacionService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> enviarCorreo(@RequestBody EmailDTO emailDTO) {
        recuperacionService.enviarCorreoRecuperacion(emailDTO.getEmail());
        return ResponseEntity.ok("Si el correo existe, se ha enviado un enlace para restablecer la contraseña.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        boolean resultado = recuperacionService.resetPassword(dto.getToken(), dto.getNewPassword());
        if (resultado) {
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } else {
            return ResponseEntity.badRequest().body("Token inválido o expirado");
        }
    }
}
