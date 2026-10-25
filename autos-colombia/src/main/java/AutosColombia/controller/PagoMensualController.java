package AutosColombia.controller;

import AutosColombia.service.PagoMensualService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PagoMensualController {

    private final PagoMensualService pagoMensualService;

    public PagoMensualController(
            PagoMensualService pagoMensualService) {
        this.pagoMensualService = pagoMensualService;
    }

    @GetMapping("/pagos")
    public String mostrarPagos(Model model) {

        model.addAttribute(
                "pagos",
                pagoMensualService.listarPagos()
        );

        return "pagos";
    }

    @PostMapping("/pagos/registrar")
    public String registrarPago(
            @RequestParam String placa,
            @RequestParam String tipoVehiculo,
            RedirectAttributes redirectAttributes) {

        try {
            pagoMensualService.registrarPagoMensual(
                    placa,
                    tipoVehiculo
            );

            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "La mensualidad se registró correctamente."
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    e.getMessage()
            );
        }

        return "redirect:/pagos";
    }
}
