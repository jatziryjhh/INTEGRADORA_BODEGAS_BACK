package mx.edu.utez.Backend.Bodegas.config.stripe;

import com.stripe.Stripe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Bean
    public String configureStripe() {
        Stripe.apiKey = "sk_test_51RDg5I2EUd41r5qmuMIZwO8M6Xo18VuQgJT3zH8GAlayFfEoufa8v2eD4xt5Ij6gEDYP020uBSGZCOaQiOynGiH8004hmA7KUR";
        return Stripe.apiKey;
    }
}

