package AutosColombia.repository;

import AutosColombia.model.PagoMensual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoMensualRepository
        extends JpaRepository<PagoMensual, Long> {

    List<PagoMensual> findByPlacaOrderByFechaPagoDesc(
            String placa
    );

    Optional<PagoMensual> findFirstByPlacaOrderByFechaPagoDesc(
            String placa
    );
}
