package mx.edu.utez.Backend.Bodegas.services;

import mx.edu.utez.Backend.Bodegas.models.bodega.BodegaBean;
import mx.edu.utez.Backend.Bodegas.models.pago.PagoBean;
import mx.edu.utez.Backend.Bodegas.models.usuario.UsuarioBean;
import mx.edu.utez.Backend.Bodegas.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class PagosService {
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CorreoService correoService;

    //REGEX patterns
    private static final String MONTO_PATTERN = "^(?!\\s*$)\\d+(\\.\\d{1,2})?$";
    private static final String FECHA_PATTERN = "^(?!\\s*$)\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public List<PagoBean> obtenerTodosLosPagos() {
        return pagoRepository.findAll();
    }

    public Optional<PagoBean> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public PagoBean crearPago(PagoBean pago) {
        validarPago(pago);
        pago.setUuid(UUID.randomUUID().toString());
        pago.setFechaPago(LocalDate.now());

        PagoBean pagoGuardado = pagoRepository.save(pago);

        enviarCorreoDePago(pagoGuardado);

        return pagoGuardado;
    }


    public Optional<PagoBean> actualizarPago(Long id, PagoBean nuevoPago) {
        return pagoRepository.findById(id)
                .map(pagoExistente -> {
                    pagoExistente.setMonto(nuevoPago.getMonto());
                    pagoExistente.setFechaPago(nuevoPago.getFechaPago());
                    pagoExistente.setBodega(nuevoPago.getBodega());
                    return pagoRepository.save(pagoExistente);
                });
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }

    public Optional<PagoBean> buscarPorUUID(String uuid) {
        return pagoRepository.findByUuid(uuid);
    }

    public void validarPago(PagoBean pago) {
        if (pago.getMonto() == null || !pago.getMonto().toString().matches(MONTO_PATTERN)) {
            throw new IllegalArgumentException("El monto no es válido");
        }
        // Solo validamos la fecha si ya fue seteada antes (como al actualizar un pago)
        if (pago.getFechaPago() != null && !pago.getFechaPago().toString().matches(FECHA_PATTERN)) {
            throw new IllegalArgumentException("La fecha no es válida");
        }
    }
    public void enviarCorreoDePago(PagoBean pago) {
        BodegaBean bodega = pago.getBodega();

        if (bodega == null || bodega.getCliente() == null) {
            throw new IllegalArgumentException("La bodega no tiene un cliente asignado. No se puede enviar el correo.");
        }

        UsuarioBean cliente = bodega.getCliente();
        String emailCliente = cliente.getEmail(); // Usamos email del cliente
        String nombreCliente = cliente.getNombre(); // O como se llame tu atributo
        String subject = "Confirmación de Pago";

        String body = "Hola " + nombreCliente + ",\n\n" +
                "Tu pago de $" + pago.getMonto() + " ha sido recibido con éxito.\n" +
                "📦 Bodega #" + bodega.getFolio() + "\n" +
                "🏢 Edificio: " + bodega.getEdificio() + "\n" +
                "📏 Tamaño: " + bodega.getTamano() + "\n" +
                "💰 Precio: $" + pago.getMonto() + "\n" +
                "📅 Fecha de vencimiento del contrato: " + pago.getFechaVencimiento() + "\n\n" +
                "Gracias por elegirnos.\n\nSaludos,\nEquipo RentABodega";

        // Enviar correo
        correoService.sendEmail(emailCliente, subject, body);

        // Esto es opcional pero útil para depurar
        System.out.println("✅ Correo enviado a: " + emailCliente);
    }

    @Scheduled(cron = "0 0 10 * * ?") // Ejecutar todos los días a las 10 AM
    public void verificarPagosPorVencer() {
        LocalDate today = LocalDate.now();
        LocalDate fechaLimite = today.plusDays(10);  // Fecha límite, 10 días después de hoy

        // Llamada al repositorio con el rango de fechas
        List<PagoBean> pagosPorVencer = pagoRepository.findByFechaVencimientoBetween(today, fechaLimite);

        for (PagoBean pago : pagosPorVencer) {
            // Verifica si ya se envió un recordatorio
            if (pago.getRecordatorioEnviado() == null || !pago.getRecordatorioEnviado()) {
                String subject = "Recordatorio: Tu renta vence en 10 días";
                String body = "Hola, \n\nTu contrato de la bodega #" + pago.getBodega().getFolio() + " vence el " + pago.getFechaVencimiento() + ".\n\nPor favor, realiza tu pago para renovar el contrato.";
                correoService.sendEmail(pago.getBodega().getCliente().getEmail(), subject, body);

                // Ahora llamamos a tu método para enviar correo de renovación o cancelación
                enviarCorreoDeRenovacionOCancelacion(pago); // Llamada al método que actualiza la fecha y manda el correo

                // Actualizar el campo recordatorio_enviado
                pago.setRecordatorioEnviado(true);
                pagoRepository.save(pago);  // Guardar los cambios
            }
        }
    }

    public void enviarCorreoDeRenovacionOCancelacion(PagoBean pago) {
        String subject = "Opción de renovación o cancelación de tu contrato";

        // Acceder al usuario (cliente) que está asociado con la bodega
        String nombreCliente = pago.getBodega().getCliente().getNombre();  // Cambio de Cliente a Usuario
        String emailCliente = pago.getBodega().getCliente().getEmail();   // Cambio de Cliente a Usuario

        String body = "Hola " + nombreCliente + ",\n\n" +
                "Tu contrato de la bodega #" + pago.getBodega().getFolio() + " está por vencer el " + pago.getFechaVencimiento() + ".\n\n" +
                "Puedes renovar tu contrato o cancelarlo.\n" +
                "Si deseas renovarlo, por favor realiza el pago correspondiente.\n\n" +
                "Si deseas cancelar, por favor contacta a nuestro equipo.\n\n" +
                "Gracias por confiar en nosotros.";

        // Enviar correo de renovación o cancelación
        correoService.sendEmail(emailCliente, subject, body);

        // Usar LocalDateTime para poder aplicar plusMinutes
        // Para 5 minutos:
        LocalDateTime nuevaFechaVencimiento = LocalDateTime.now().plusMinutes(5);  // 5 minutos después de la hora actual
        pago.setFechaVencimiento(nuevaFechaVencimiento.toLocalDate());  // Convertir de LocalDateTime a LocalDate para guardarlo

        // Guardar los cambios después de actualizar la fecha
        pagoRepository.save(pago);  // Guardar los cambios después de actualizar la fecha
    }

}