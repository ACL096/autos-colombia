package AutosColombia.service;

import AutosColombia.model.Celda;
import AutosColombia.repository.CeldaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class CeldaService {

    private final CeldaRepository celdaRepository;

    public CeldaService(CeldaRepository celdaRepository) {
        this.celdaRepository = celdaRepository;

        inicializarCeldas();
    }

    private void inicializarCeldas() {
        for (int i = 1; i <= 10; i++) {
            String numero = String.format("C%02d", i);

            if (!celdaRepository.existsById(numero)) {
                celdaRepository.save(new Celda(numero));
            }
        }
    }

    public List<Celda> listarCeldas() {
        return celdaRepository.findAll()
                .stream()
                .sorted((a, b) ->
                        a.getNumero().compareTo(b.getNumero()))
                .toList();
    }

    public long contarCeldasDisponibles() {
        return celdaRepository.findAll()
                .stream()
                .filter(celda -> !celda.isOcupada())
                .count();
    }

    @Transactional
    public Celda asignarCeldaDisponible(String placa) {

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "La placa es obligatoria para asignar una celda."
            );
        }

        String placaNormalizada =
                placa.trim().toUpperCase(Locale.ROOT);

        boolean yaTieneCelda = celdaRepository.findAll()
                .stream()
                .anyMatch(celda ->
                        celda.isOcupada()
                                && placaNormalizada.equals(
                                celda.getPlacaVehiculo())
                );

        if (yaTieneCelda) {
            throw new IllegalArgumentException(
                    "Este vehículo ya tiene una celda asignada."
            );
        }

        Celda celdaDisponible = celdaRepository.findAll()
                .stream()
                .filter(celda -> !celda.isOcupada())
                .sorted((a, b) ->
                        a.getNumero().compareTo(b.getNumero()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay celdas disponibles en el parqueadero."
                ));

        celdaDisponible.setOcupada(true);
        celdaDisponible.setPlacaVehiculo(placaNormalizada);

        return celdaRepository.save(celdaDisponible);
    }

    @Transactional
    public Celda liberarCelda(String placa) {

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "La placa es obligatoria para liberar una celda."
            );
        }

        String placaNormalizada =
                placa.trim().toUpperCase(Locale.ROOT);

        Celda celdaOcupada = celdaRepository.findAll()
                .stream()
                .filter(Celda::isOcupada)
                .filter(celda ->
                        placaNormalizada.equals(
                                celda.getPlacaVehiculo())
                )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró una celda ocupada por esa placa."
                ));

        celdaOcupada.setOcupada(false);
        celdaOcupada.setPlacaVehiculo(null);

        return celdaRepository.save(celdaOcupada);
    }
}
