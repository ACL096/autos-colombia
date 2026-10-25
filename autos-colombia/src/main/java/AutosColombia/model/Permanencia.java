package AutosColombia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "permanencias")
public class Permanencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;

    private String tipoVehiculo;

    private LocalDateTime fechaHoraEntrada;

    private LocalDateTime fechaHoraSalida;

    private String numeroCelda;

    private Long valorCobrado;

    private String modalidadCobro;

    public Permanencia() {
    }

    public Permanencia(
            Long id,
            String placa,
            String tipoVehiculo,
            LocalDateTime fechaHoraEntrada,
            String numeroCelda) {

        this.id = id;
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.fechaHoraSalida = null;
        this.numeroCelda = numeroCelda;
        this.valorCobrado = null;
        this.modalidadCobro = null;
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

    public LocalDateTime getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public void setFechaHoraEntrada(
            LocalDateTime fechaHoraEntrada) {
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(
            LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getNumeroCelda() {
        return numeroCelda;
    }

    public void setNumeroCelda(String numeroCelda) {
        this.numeroCelda = numeroCelda;
    }

    public Long getValorCobrado() {
        return valorCobrado;
    }

    public void setValorCobrado(Long valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public String getModalidadCobro() {
        return modalidadCobro;
    }

    public void setModalidadCobro(String modalidadCobro) {
        this.modalidadCobro = modalidadCobro;
    }

    public boolean estaActiva() {
        return fechaHoraSalida == null;
    }
}
