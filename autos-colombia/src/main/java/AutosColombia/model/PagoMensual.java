package AutosColombia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos_mensuales")
public class PagoMensual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;

    private String tipoVehiculo;

    private LocalDateTime fechaPago;

    private LocalDate fechaInicio;

    private LocalDate fechaVencimiento;

    private Long valor;

    public PagoMensual() {
    }

    public PagoMensual(
            String placa,
            String tipoVehiculo,
            LocalDateTime fechaPago,
            LocalDate fechaInicio,
            LocalDate fechaVencimiento,
            Long valor) {

        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaPago = fechaPago;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();

        return fechaInicio != null
                && fechaVencimiento != null
                && !hoy.isBefore(fechaInicio)
                && !hoy.isAfter(fechaVencimiento);
    }
}
