package mx.edu.utez.Backend.Bodegas.controlller;

import mx.edu.utez.Backend.Bodegas.models.pago.PagoBean;
import mx.edu.utez.Backend.Bodegas.services.PagosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.edu.utez.Backend.Bodegas.services.StripeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/pagos/")
public class PagosController {
    @Autowired
    private PagosService pagoService;

    @Autowired
    private StripeService stripeService;

    @GetMapping
    public List<PagoBean> obtenerTodosLosPagos() {
        return pagoService.obtenerTodosLosPagos();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<PagoBean> buscarPorId(@PathVariable Long id) {
        Optional<PagoBean> pago = pagoService.buscarPorId(id);
        return pago.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<PagoBean> buscarPorUUID(@PathVariable String uuid) {
        Optional<PagoBean> pago = pagoService.buscarPorUUID(uuid);
        return pago.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/stripe")
    public ResponseEntity<?> crearPagoConStripe(@RequestBody PagoBean pago) {
        try {
            String clientSecret = stripeService.crearPaymentIntent(pago.getMonto(), "usd", pago.getCliente().getUuid());
            PagoBean nuevoPago = pagoService.crearPago(pago);
            return ResponseEntity.ok(new PaymentResponse(clientSecret, nuevoPago.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear el pago con Stripe: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<PagoBean>> actualizarPago(@PathVariable Long id, @RequestBody PagoBean nuevoPago) {
        Optional<PagoBean> pagoActualizado = pagoService.actualizarPago(id, nuevoPago);
        return ResponseEntity.ok(pagoActualizado);
    }

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

    public static class PaymentResponse{
        private String clientSecret;
        private Long pagoId;

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
    }

}
