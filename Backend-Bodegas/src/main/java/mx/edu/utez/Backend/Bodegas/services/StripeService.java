package mx.edu.utez.Backend.Bodegas.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import mx.edu.utez.Backend.Bodegas.repositories.PagoRepository;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private final PagoRepository pagosRepository;

    // Constructor donde se configura la clave de API de Stripe
    public StripeService(PagoRepository pagosRepository) {
        this.pagosRepository = pagosRepository;
        Stripe.apiKey = "sk_test_51RDg5I2EUd41r5qmuMIZwO8M6Xo18VuQgJT3zH8GAlayFfEoufa8v2eD4xt5Ij6gEDYP020uBSGZCOaQiOynGiH8004hmA7KUR"; // Establecemos la clave secreta de Stripe
    }

    // Método para crear un PaymentIntent en Stripe
    public PaymentIntent createPaymentIntent(Double monto) throws StripeException {

        // Definir los parámetros para el PaymentIntent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (monto * 100))  // Convertir a centavos (Stripe maneja los pagos en centavos)
                .setCurrency("mxn")  // Definir la moneda (en este caso, pesos mexicanos)
                .build();

        // Crear el PaymentIntent
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Devolver el PaymentIntent
        return paymentIntent;
    }
}
