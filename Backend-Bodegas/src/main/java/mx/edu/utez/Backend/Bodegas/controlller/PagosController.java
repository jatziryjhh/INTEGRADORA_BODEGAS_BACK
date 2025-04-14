package mx.edu.utez.Backend.Bodegas.controlller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.models.pago.PagoBean;
import mx.edu.utez.Backend.Bodegas.services.BodegasService;
import mx.edu.utez.Backend.Bodegas.services.PagosService;
import mx.edu.utez.Backend.Bodegas.services.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(pagoRequest.getMonto());
            PagoBean nuevoPago = new PagoBean();
            nuevoPago.setMonto(pagoRequest.getMonto());
            nuevoPago.setBodega(bodega);  // Usar la bodega obtenida
            nuevoPago.setStatus("CREATED");
            pagoService.crearPago(nuevoPago);
            return ResponseEntity.ok(new PaymentResponse(paymentIntent.getClientSecret(), nuevoPago.getId()));

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
        private String clienteId;
        private Long bodegaId;

        public Double getMonto() {
            return monto;
        }

        public void setMonto(Double monto) {
            this.monto = monto;
        }

        public String getClienteId() {
            return clienteId;
        }

        public void setClienteId(String clienteId) {
            this.clienteId = clienteId;
        }

        public Long getBodegaId() {
            return bodegaId;
        }

        public void setBodegaId(Long bodegaId) {
            this.bodegaId = bodegaId;
        }
    }
}