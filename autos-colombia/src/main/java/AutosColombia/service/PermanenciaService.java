package AutosColombia.service;

import AutosColombia.model.Celda;
import AutosColombia.model.Permanencia;
import AutosColombia.repository.PermanenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class PermanenciaService {

    private final PermanenciaRepository permanenciaRepository;
    private final CeldaService celdaService;
    private final PagoMensualService pagoMensualService;

    public PermanenciaService(
            PermanenciaRepository permanenciaRepository,
            CeldaService celdaService,
            PagoMensualService pagoMensualService) {

        this.permanenciaRepository = permanenciaRepository;
        this.celdaService = celdaService;
        this.pagoMensualService = pagoMensualService;
    }

    public Permanencia registrarEntrada(
            String placa,
            String tipoVehiculo) {

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "La placa es obligatoria."
            );
        }

        if (tipoVehiculo == null || tipoVehiculo.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo de vehículo es obligatorio."
            );
        }

        String placaNormalizada =
                placa.trim().toUpperCase(Locale.ROOT);

        String tipoNormalizado = tipoVehiculo.trim();

        boolean yaEstaDentro = permanenciaRepository.findAll()
                .stream()
                .anyMatch(p ->
                        p.getPlaca().equals(placaNormalizada)
                                && p.estaActiva()
                );

        if (yaEstaDentro) {
            throw new IllegalArgumentException(
                    "Este vehículo ya tiene una permanencia activa."
            );
        }

        Celda celdaAsignada =
                celdaService.asignarCeldaDisponible(
                        placaNormalizada
                );

        Permanencia permanencia = new Permanencia(
                null,
                placaNormalizada,
                tipoNormalizado,
                LocalDateTime.now(),
                celdaAsignada.getNumero()
        );

        return permanenciaRepository.save(permanencia);
    }

    public List<Permanencia> listarPermanencias() {
        return permanenciaRepository.findAll();
    }

    @Transactional
    public Permanencia registrarSalida(String placa) {

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "La placa es obligatoria."
            );
        }

        String placaNormalizada =
                placa.trim().toUpperCase(Locale.ROOT);

        Permanencia permanenciaActiva =
                permanenciaRepository.findAll()
                        .stream()
                        .filter(p ->
                                p.getPlaca().equals(
                                        placaNormalizada
                                )
                        )
                        .filter(Permanencia::estaActiva)
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No se encontró una permanencia activa para esa placa."
                                )
                        );

LocalDateTime fechaSalida = LocalDateTime.now();

        boolean tieneMensualidad =
                pagoMensualService.tieneMensualidadVigente(
                        placaNormalizada
                );

        if (tieneMensualidad) {

            permanenciaActiva.setValorCobrado(0L);
            permanenciaActiva.setModalidadCobro("Mensualidad");

        } else {

            long valor = pagoMensualService.calcularCobroPorHoras(
                    permanenciaActiva.getTipoVehiculo(),
                    permanenciaActiva.getFechaHoraEntrada(),
                    fechaSalida
            );

            permanenciaActiva.setValorCobrado(valor);
            permanenciaActiva.setModalidadCobro("Por hora");
        }

        permanenciaActiva.setFechaHoraSalida(fechaSalida);

        celdaService.liberarCelda(placaNormalizada);

        return permanenciaRepository.save(permanenciaActiva);
    }
}
