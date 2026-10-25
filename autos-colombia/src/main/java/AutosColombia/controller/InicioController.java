package AutosColombia.controller;

import AutosColombia.model.Permanencia;
import AutosColombia.service.CeldaService;
import AutosColombia.service.PermanenciaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InicioController {

    private final PermanenciaService permanenciaService;
    private final CeldaService celdaService;

    public InicioController(
            PermanenciaService permanenciaService,
            CeldaService celdaService) {

        this.permanenciaService = permanenciaService;
        this.celdaService = celdaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute(
                "permanencias",
                permanenciaService.listarPermanencias()
        );

        model.addAttribute(
                "celdas",
                celdaService.listarCeldas()
        );

        model.addAttribute(
                "celdasDisponibles",
                celdaService.contarCeldasDisponibles()
        );

        return "index";
    }

    @PostMapping("/registrar-entrada")
    public String registrarEntrada(
            @RequestParam String placa,
            @RequestParam String tipoVehiculo,
            RedirectAttributes redirectAttributes) {

        try {
            Permanencia permanencia =
                    permanenciaService.registrarEntrada(
                            placa,
                            tipoVehiculo
                    );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "Entrada registrada correctamente para la placa "
                            + permanencia.getPlaca()
                            + ". Celda asignada: "
                            + permanencia.getNumeroCelda() + "."
            );

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/";
    }

    @PostMapping("/registrar-salida")
    public String registrarSalida(
            @RequestParam String placa,
            RedirectAttributes redirectAttributes) {

        try {
            Permanencia permanencia =
                    permanenciaService.registrarSalida(placa);

            String mensaje =
                    "Salida registrada correctamente para la placa "
                            + permanencia.getPlaca()
                            + ". Se liberó la celda "
                            + permanencia.getNumeroCelda()
                            + ". ";

            if ("Mensualidad".equals(
                    permanencia.getModalidadCobro())) {

                mensaje +=
                        "El vehículo tiene mensualidad vigente. "
                                + "Valor adicional por esta permanencia: "
                                + "$0 COP.";

            } else {

                mensaje +=
                        "Modalidad de cobro: "
                                + permanencia.getModalidadCobro()
                                + ". Valor cobrado: $"
                                + permanencia.getValorCobrado()
                                + " COP.";
            }

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    mensaje
            );

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/";
    }
}
