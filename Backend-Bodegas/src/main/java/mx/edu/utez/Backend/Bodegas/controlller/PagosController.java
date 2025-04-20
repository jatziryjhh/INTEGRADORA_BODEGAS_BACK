package mx.edu.utez.Backend.Bodegas.controlller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.models.pago.PagoBean;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.BodegasRepository;
import mx.edu.utez.Backend.Bodegas.repositories.UsuarioRepository;
import mx.edu.utez.Backend.Bodegas.services.BodegasService;
import mx.edu.utez.Backend.Bodegas.services.CorreoService;
import mx.edu.utez.Backend.Bodegas.services.PagosService;
import mx.edu.utez.Backend.Bodegas.services.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/pagos/")
public class PagosController {
    @Autowired
    private PagosService pagoService;

    @Autowired
    private BodegasService bodegasService;

    @Autowired
    private StripeService stripeService;
    @Autowired
    private CorreoService correoService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BodegasRepository   bodegaRepository;

    // Obtener todos los pagos
    @GetMapping
    public List<PagoBean> obtenerTodosLosPagos() {
        return pagoService.obtenerTodosLosPagos();
    }

    // Buscar pago por ID
    @GetMapping("id/{id}")
    public ResponseEntity<PagoBean> buscarPorId(@PathVariable Long id) {
        Optional<PagoBean> pago = pagoService.buscarPorId(id);
        return pago.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar pago por UUID
    @GetMapping("uuid/{uuid}")
    public ResponseEntity<PagoBean> buscarPorUUID(@PathVariable String uuid) {
        Optional<PagoBean> pago = pagoService.buscarPorUUID(uuid);
        return pago.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear PaymentIntent para Stripe
    @PostMapping("/stripe")
    public ResponseEntity<PaymentResponse> crearPago(@RequestBody PagoRequest pagoRequest) {
        try {
            if (pagoRequest.getMonto() == null || pagoRequest.getMonto() <= 0) {
                return ResponseEntity.badRequest().body(new PaymentResponse("Monto inválido", null));
            }
            Long bodegaId = pagoRequest.getBodegaId();
            if (bodegaId == null || bodegaId <= 0) {
                return ResponseEntity.badRequest().body(new PaymentResponse("ID de bodega inválido", null));
            }
            Optional<BodegaBean> bodegaOptional = bodegasService.BuscarID(bodegaId);
            if (bodegaOptional.isEmpty()) {
                return ResponseEntity.status(404).body(new PaymentResponse("Bodega no encontrada con ID: " + bodegaId, null));
            }
            BodegaBean bodega = bodegaOptional.get();

            // Asegurar que la bodega tiene cliente asignado
            if (bodega.getCliente() == null && pagoRequest.getClienteId() != null) {
                UsuarioBean cliente = usuarioRepository.findById(pagoRequest.getClienteId()).orElse(null);
                if (cliente != null) {
                    bodega.setCliente(cliente);
                    bodegaRepository.save(bodega);
                }
            }

            // Volver a cargar la bodega actualizada con cliente
            bodega = bodegaRepository.findById(bodega.getId()).orElse(bodega);

            PaymentIntent paymentIntent = stripeService.createPaymentIntent(pagoRequest.getMonto());

            PagoBean nuevoPago = new PagoBean();
            nuevoPago.setMonto(pagoRequest.getMonto());
            nuevoPago.setBodega(bodega);  // Usar la bodega obtenida
            nuevoPago.setStatus("CREATED");
            // Guardar el pago
            PagoBean pagoGuardado = pagoService.crearPago(nuevoPago);

            // Enviar correo de confirmación de pago
            pagoService.enviarCorreoDePago(pagoGuardado);  // Aquí se envía el correo
            pagoService.enviarCorreoDeRenovacionOCancelacion(pagoGuardado);
            // Responder con el cliente secret de Stripe
            return ResponseEntity.ok(new PaymentResponse(paymentIntent.getClientSecret(), pagoGuardado.getId()));

        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new PaymentResponse("Error al procesar el pago con Stripe", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new PaymentResponse("Error general: " + e.getMessage(), null));
        }
    }
    // Actualizar pago
    @PutMapping("/{id}")
    public ResponseEntity<PagoBean> actualizarPago(@PathVariable Long id, @RequestBody PagoBean nuevoPago) {
        Optional<PagoBean> pagoActualizado = pagoService.actualizarPago(id, nuevoPago);
        return pagoActualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        Optional<PagoBean> pago = pagoService.buscarPorId(id);
        if (pago.isPresent()) {
            pagoService.eliminarPago(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Respuesta de PaymentIntent
    public static class PaymentResponse {
        private String clientSecret;
        private Long pagoId;
        private String errorMessage;

        public PaymentResponse(String clientSecret, Long pagoId) {
            this.clientSecret = clientSecret;
            this.pagoId = pagoId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public Long getPagoId() {
            return pagoId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    // Clase de solicitud para recibir los datos del frontend
    public static class PagoRequest {
        private Double monto;
        private Long clienteId;
        private Long bodegaId;

        public Double getMonto() {
            return monto;
        }

        public void setMonto(Double monto) {
            this.monto = monto;
        }

        public Long getClienteId() {
            return clienteId;
        }

        public void setClienteId(Long clienteId) {
            this.clienteId = clienteId;
        }

        public Long getBodegaId() {
            return bodegaId;
        }

        public void setBodegaId(Long bodegaId) {
            this.bodegaId = bodegaId;
        }
    }

    // Método auxiliar para obtener el ID del cliente (debe ajustarse según la lógica de tu aplicación)
    private Long getClienteId() {
        // Aquí debes implementar la lógica para obtener el cliente asociado, como un ID de sesión o algo similar
        return 1L; // Este es solo un ejemplo; asegúrate de que sea un cliente válido
    }
}