package mx.edu.utez.Backend.Bodegas.services;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    static {
        Stripe.apiKey="sk_test_51RDg5I2EUd41r5qmuMIZwO8M6Xo18VuQgJT3zH8GAlayFfEoufa8v2eD4xt5Ij6gEDYP020uBSGZCOaQiOynGiH8004hmA7KUR";
    }
    public String crearPaymentIntent(Double monto, String moneda, String clienteId) throws Exception {
        // Convertimos el monto a centavos, ya que Stripe maneja centavos
        long amountInCents = Math.round(monto * 100);

        // Creamos el PaymentIntent con Stripe
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(moneda)
                .setCustomer(clienteId)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Retornamos el clientSecret que se utilizará en el frontend
        return paymentIntent.getClientSecret();
    }
}
