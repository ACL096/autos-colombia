package AutosColombia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "celdas")
public class Celda {

    @Id
    private String numero;

    private boolean ocupada;

    private String placaVehiculo;

    public Celda() {
    }

    public Celda(String numero) {
        this.numero = numero;
        this.ocupada = false;
        this.placaVehiculo = null;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}

