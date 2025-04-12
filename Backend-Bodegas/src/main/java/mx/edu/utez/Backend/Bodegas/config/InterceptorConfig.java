package mx.edu.utez.Backend.Bodegas.config;

import mx.edu.utez.Backend.Bodegas.models.bitacora.BitacoraInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final BitacoraInterceptor bitacoraInterceptor;

    public InterceptorConfig(BitacoraInterceptor bitacoraInterceptor) {
        this.bitacoraInterceptor = bitacoraInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bitacoraInterceptor)
                .addPathPatterns("/**") // Registra todas las rutas
                .excludePathPatterns("/auth/**"); // Puedes excluir rutas si quieres
    }
}
