package AutosColombia.service;

import AutosColombia.model.PagoMensual;
import AutosColombia.repository.PagoMensualRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class PagoMensualService {

    private static final long TARIFA_HORA_MOTO = 3000L;
    private static final long TARIFA_HORA_OTRO_VEHICULO = 6000L;

    private static final long TARIFA_MENSUAL_MOTO = 80000L;
    private static final long TARIFA_MENSUAL_OTRO_VEHICULO = 120000L;

    private final PagoMensualRepository pagoMensualRepository;

    public PagoMensualService(
            PagoMensualRepository pagoMensualRepository) {
        this.pagoMensualRepository = pagoMensualRepository;
    }

    private String normalizarTipoVehiculo(String tipoVehiculo) {
        if (tipoVehiculo == null || tipoVehiculo.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo de vehículo es obligatorio."
            );
        }

        return tipoVehiculo.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizarPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "La placa del vehículo es obligatoria."
            );
        }

        return placa.trim().toUpperCase(Locale.ROOT);
    }

    public long calcularTarifaPorHora(String tipoVehiculo) {
        String tipo = normalizarTipoVehiculo(tipoVehiculo);

        if (tipo.equals("motocicleta") || tipo.equals("moto")) {
            return TARIFA_HORA_MOTO;
        }

        return TARIFA_HORA_OTRO_VEHICULO;
    }

    public long calcularTarifaMensual(String tipoVehiculo) {
        String tipo = normalizarTipoVehiculo(tipoVehiculo);

        if (tipo.equals("motocicleta") || tipo.equals("moto")) {
            return TARIFA_MENSUAL_MOTO;
        }

        return TARIFA_MENSUAL_OTRO_VEHICULO;
    }

    public long calcularCobroPorHoras(
            String tipoVehiculo,
            LocalDateTime fechaHoraEntrada,
            LocalDateTime fechaHoraSalida) {

        if (fechaHoraEntrada == null || fechaHoraSalida == null) {
            throw new IllegalArgumentException(
                    "Las fechas de entrada y salida son obligatorias."
            );
        }

        if (fechaHoraSalida.isBefore(fechaHoraEntrada)) {
            throw new IllegalArgumentException(
                    "La salida no puede ser anterior a la entrada."
            );
        }

        Duration duracion = Duration.between(
                fechaHoraEntrada,
                fechaHoraSalida
        );

        if (duracion.isZero()) {
            return 0L;
        }

        long segundos = duracion.getSeconds();

        if (duracion.getNano() > 0) {
            segundos++;
        }

        long horasIniciadas = (segundos + 3599L) / 3600L;

        return horasIniciadas
                * calcularTarifaPorHora(tipoVehiculo);
    }

    @Transactional
    public PagoMensual registrarPagoMensual(
            String placa,
            String tipoVehiculo) {

        String placaNormalizada = normalizarPlaca(placa);
        String tipoNormalizado = normalizarTipoVehiculo(tipoVehiculo);

        LocalDate hoy = LocalDate.now();

        boolean tienePagoVigente = pagoMensualRepository
                .findByPlacaOrderByFechaPagoDesc(placaNormalizada)
                .stream()
                .anyMatch(PagoMensual::estaVigente);

        if (tienePagoVigente) {
            throw new IllegalArgumentException(
                    "Este vehículo ya tiene una mensualidad vigente."
            );
        }

        LocalDate fechaInicio = hoy;
        LocalDate fechaVencimiento = hoy.plusMonths(1).minusDays(1);

        long valor = calcularTarifaMensual(tipoNormalizado);

        PagoMensual pago = new PagoMensual(
                placaNormalizada,
                tipoNormalizado,
                LocalDateTime.now(),
                fechaInicio,
                fechaVencimiento,
                valor
        );

        return pagoMensualRepository.save(pago);
    }

    public List<PagoMensual> listarPagos() {
        return pagoMensualRepository.findAll()
                .stream()
                .sorted((a, b) ->
                        b.getFechaPago().compareTo(a.getFechaPago()))
                .toList();
    }

    public List<PagoMensual> listarPagosPorPlaca(String placa) {
        String placaNormalizada = normalizarPlaca(placa);

        return pagoMensualRepository
                .findByPlacaOrderByFechaPagoDesc(placaNormalizada);
    }

    public boolean tieneMensualidadVigente(String placa) {
        String placaNormalizada = normalizarPlaca(placa);

        return pagoMensualRepository
                .findByPlacaOrderByFechaPagoDesc(placaNormalizada)
                .stream()
                .anyMatch(PagoMensual::estaVigente);
    }
}
